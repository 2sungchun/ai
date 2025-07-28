import os
import time
import json
from json import JSONDecodeError 

from fastapi import FastAPI, Request, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse

import uvicorn
from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage, SystemMessage
from langchain_core.output_parsers import StrOutputParser 
from langchain_core.prompts import ChatPromptTemplate, PromptTemplate
from langchain.output_parsers.structured import ResponseSchema, StructuredOutputParser
from langchain_core.exceptions import OutputParserException

import apitool

llm = ChatOpenAI(model="gpt-4o-mini", temperature=0) # Key는 환경 변수 자동 인식, OPENAI_API_KEY

app = FastAPI()

dotenv_path = './env.txt'
load_dotenv(dotenv_path)

SpringBoot_FastAPI_KEY = os.getenv('SpringBoot_FastAPI_KEY')

# 확인
print("-> SpringBoot_FastAPI_KEY Loaded key:", SpringBoot_FastAPI_KEY)

# CORS 설정 (브라우저 스크립트용)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/") # http://localhost:8000
def test():
    return {"resort": "Spring Boot resort_v7sbm3c_llm_auth"}

@app.post("/emotion")
async def emotion_proc(request: Request):
    print('-> emotion_proc 호출됨')
  
    # 1) JSON 파싱 에러 처리
    try:
        data = await request.json()
    except Exception as e:
        return JSONResponse(
            status_code=400,
            content={"error": "유효하지 않은 JSON입니다.", "detail": str(e)}
        )

    # 2) 본 처리 및 일반 예외 처리
    try:
        # 키 검증
        if data.get("SpringBoot_FastAPI_KEY") != SpringBoot_FastAPI_KEY:
            return JSONResponse(
                status_code=401,
                content={"error": "정상적이지 않은 접근입니다."}
            )
            
        # 1) 데이터 준비  
        # content = data['content']
        print("-> data: ", data)
        
        content = data['content']
        content = apitool.remove_empty_lines(content)

        # 2) 출력 스키마 & 파서 설정
        response_schemas = [
            ResponseSchema(name="res", description="{'res': 1 또는 0}")
        ]
        output_parser = StructuredOutputParser.from_response_schemas(response_schemas)
        format_instructions = output_parser.get_format_instructions()

        # 3) PromptTemplate 정의
        prompt = PromptTemplate.from_template(
            "{system}\n"
            "아래 댓글이 부정적인지 긍정적인지 알려줘, 출력은 JSON 형태로 처리해줘(긍정: 1, 부정: 0).\n\n{content}.\n\n"
            "{format_instructions}"
        )

        inputs = {
            "system": "너는 댓글 긍정, 부정 판단 시스템이야",
            "content": content,
            "format_instructions": format_instructions
        }

        # (디버깅용) 실제 프롬프트 문자열 확인
        # rendered = prompt.format(**inputs)
        # print("-> prompt:", rendered)

        # 4) 파이프라인 실행 & 결과 출력 (Prompt → LLM → Parser)
        pipeline = prompt | llm | output_parser

        for attempt in range(3): # 0 ~ 2
            try:
                result = pipeline.invoke(inputs)
                break
            except (OutputParserException) as e:
                if attempt < 2:
                    continue
                else:
                    raise # 에러 출력
        
        print('-> result: ', result)
        
        return result
    
    except Exception as e:
        # 내부 서버 오류
        return JSONResponse(
            status_code=500,
            content={"error": "서버 내부 오류가 발생했습니다.", "detail": str(e)}
        )        

@app.post("/summary")
async def summary_proc(request: Request):
    # 1) JSON 파싱 에러 처리
    try:
        data = await request.json()
    except Exception as e:
        return JSONResponse(
            status_code=400,
            content={"error": "유효하지 않은 JSON입니다.", "detail": str(e)}
        )

    # 2) 본 처리 및 일반 예외 처리
    try:
        # 키 검증
        if data.get("SpringBoot_FastAPI_KEY") != SpringBoot_FastAPI_KEY:
            return JSONResponse(
                status_code=401,
                content={"error": "정상적이지 않은 접근입니다."}
            )
            
        # 1) 데이터 준비  
        content = data['content']
        content = apitool.remove_empty_lines(content)

        # 2) 출력 스키마 & 파서 설정
        response_schemas = [
            ResponseSchema(name="res", description="{'res': 요약된 문장}")
        ]
        output_parser = StructuredOutputParser.from_response_schemas(response_schemas)
        format_instructions = output_parser.get_format_instructions()

        # 3) PromptTemplate 정의
        prompt = PromptTemplate.from_template(
            "{system}\n"
            "아래 문장을 200자 이내로 한글로 요약해줘.\n\n{content}.\n\n"
            "{format_instructions}"
        )

        inputs = {
            "system": "너는 요약 시스템이야",
            "content": content,
            "format_instructions": format_instructions
        }

        # (디버깅용) 실제 프롬프트 문자열 확인
        # rendered = prompt.format(**inputs)
        # print("-> prompt:", rendered)

        # 4) 파이프라인(체인) 구성 및 실행
        pipeline = prompt | llm | output_parser
        result = pipeline.invoke(inputs)

        for attempt in range(3): # 0 ~ 2
            try:
                result = pipeline.invoke(inputs)
                break
            except (OutputParserException) as e:
                if attempt < 2:
                    continue
                else:
                    raise # 에러 출력
                
        print("-> result:", result)
        
        return result
    
    except Exception as e:
        # 내부 서버 오류
        return JSONResponse(
            status_code=500,
            content={"error": "서버 내부 오류가 발생했습니다.", "detail": str(e)}
        )

# 메일 번역        
@app.post("/translator")
async def mail_translator_proc(request: Request):
    # 1) JSON 파싱 에러 처리
    try:
        data = await request.json()
    except Exception as e:
        return JSONResponse(
            status_code=400,
            content={"error": "유효하지 않은 JSON입니다.", "detail": str(e)}
        )

    # 2) 본 처리 및 일반 예외 처리
    try:
        # 키 검증
        if data.get("SpringBoot_FastAPI_KEY") != SpringBoot_FastAPI_KEY:
            return JSONResponse(
                status_code=401,
                content={"error": "정상적이지 않은 접근입니다."}
            )
            
        # 1) 데이터 준비  
        title = data['title']
        content = data['content']
        content = apitool.remove_empty_lines(content)
        language = data['language']
        
        # 2) 출력 스키마 & 파서 설정
        response_schemas = [
            ResponseSchema(name="res", description="{'title': 번역된 문장, 'content': 번역된 문장}")
        ]
        output_parser = StructuredOutputParser.from_response_schemas(response_schemas)
        format_instructions = output_parser.get_format_instructions()

        # 3) PromptTemplate 정의
        prompt = PromptTemplate.from_template(
            "{system}\n"
            "아래 문장을 {language}로 번역해줘.\n\n"
            "{title}\n\n"    
            "{content}\n\n"    
            "{format_instructions}"
        )

        inputs = {
            "system": "너는 번역 시스템이야",
            "language": language,
            "title": title,
            "content": content,
            "format_instructions": format_instructions
        }

        # (디버깅용) 실제 프롬프트 문자열 확인
        # rendered = prompt.format(**inputs)
        # print("-> prompt:", rendered)

        # 4) 파이프라인(체인) 구성 및 실행
        pipeline = prompt | llm | output_parser
        result = pipeline.invoke(inputs)

        for attempt in range(3): # 0 ~ 2
            try:
                result = pipeline.invoke(inputs)
                break
            except (OutputParserException) as e:
                if attempt < 2:
                    continue
                else:
                    raise # 에러 출력
                
        print("-> result:", result)
        
        return result
    
    except Exception as e:
        # 내부 서버 오류
        return JSONResponse(
            status_code=500,
            content={"error": "서버 내부 오류가 발생했습니다.", "detail": str(e)}
        )        

if __name__ == "__main__":
    # uvicorn.run("resort_auth:app", host="121.78.128.17", port=8000, reload=True) # Gabia 할당 불가
    # resort_v7sbm3c_llm_auth.py
    uvicorn.run("resort_v7sbm3c_llm_auth:app", host="0.0.0.0", port=8000, reload=True)
    
    
'''
activate ai
python resort_v7sbm3c_llm_auth.py
'''