import os
import time

from fastapi import FastAPI, Request, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
import uvicorn
from dotenv import load_dotenv
import json

from openai import OpenAI

import apitool

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

@app.get("/")
def test():
    return {"resort": "Spring Boot resort_v7sbm3c_llm_auth"}

@app.post("/translator")
async def translator_proc(request: Request):
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

        # 번역 요청
        sentence = data.get("sentence")
        language = data.get("language")
        age = data.get("age")

        prompt = (
            f"아래 문장을 {age}살 수준의 {language}로 "
            f"존댓말로 간결히 번역해줘.\n\n{sentence}"
        )
        fmt = '{ "res": "번역된 문장" }'
        response = apitool.answer(
            "너는 번역가야",
            prompt,
            fmt,
            llm="gpt-4.1-mini"
        )
        
        return response

    except Exception as e:
        # 내부 서버 오류
        return JSONResponse(
            status_code=500,
            content={"error": "서버 내부 오류가 발생했습니다.", "detail": str(e)}
        )

@app.post("/movie")
async def movie_proc(request: Request):
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

        # 추천 요청
        movie = data.get("movie")
        movie = list(map(int, movie.split(",")))
        movies = [
            '반지의 제왕', 'A Quiet Place (2018)', '러브액츄얼리', '화이트 칙스', 'Interstellar (2014)',
            '해리포터와 마법사의 돌', 'The Autopsy of Jane Doe (2016)', '타이타닉', '세 얼간이', 'A.I. (2001)',
            '캐리비안의 해적', 'The Conjuring (2013)', '맘마미아', '덤 앤 더머', 'The Martian (2015)',
            '닥터 스트레인지', 'The Exorcist (1973)', 'La La Land (2016)', '우리는 동물원을 샀다.', 'Edge of Tomorrow (2014)',
            '아바타 (2009)', 'The Rite (2011)', '비긴 어게인', '미트 페어런츠', 'Gravity (2013)'
        ]
        watch = [m for idx, m in enumerate(movies) if movie[idx] == 1]
        watch_join = ",".join(watch)
        prompt = (
            f'내가 시청한 영화는 [{watch_join}]이야, '
            '2000년 이후 출시된 영화 중 내가 시청하지 않은 것 중 평점 높은 5편 추천해줘.'
        )
        fmt = '{ "res": "[\'영화1\', \'영화2\', ...]" }'
        response = apitool.answer('너는 영화 추천 시스템이야', prompt, fmt)
        return response

    except Exception as e:
        # 내부 서버 오류
        return JSONResponse(
            status_code=500,
            content={"error": "서버 내부 오류가 발생했습니다.", "detail": str(e)}
        )

@app.post("/genre")
async def genre_proc(request: Request):
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

        # 관심분야 분류 요청
        movie = data.get("movie")

        movie = list(map(int, movie.split(",")))
        movies = [
            '반지의 제왕', 'A Quiet Place (2018)', '러브액츄얼리', '화이트 칙스', 'Interstellar (2014)',
            '해리포터와 마법사의 돌', 'The Autopsy of Jane Doe (2016)', '타이타닉', '세 얼간이', 'A.I. (2001)',
            '캐리비안의 해적', 'The Conjuring (2013)', '맘마미아', '덤 앤 더머', 'The Martian (2015)',
            '닥터 스트레인지', 'The Exorcist (1973)', 'La La Land (2016)', '우리는 동물원을 샀다.', 'Edge of Tomorrow (2014)',
            '아바타 (2009)', 'The Rite (2011)', '비긴 어게인', '미트 페어런츠', 'Gravity (2013)'
        ]
        watch = [m for idx, m in enumerate(movies) if movie[idx] == 1]
        watch_join = ",".join(watch)
        prompt = (
            f'내가 시청한 영화는 [{watch_join}]이야, '
            '아래 장르 목록 중 가장 유사한 장르 두 개만 골라줘.\n'
            '[장르 목록: "판타지", "공포", "로맨스/멜로", "코미디", "SF 영화"]'
        )
        fmt = '{ "res": "장르1/장르2" }'
        response = apitool.answer('너는 장르 판정 시스템이야', prompt, fmt)
        return response

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

        # 관심분야 분류 요청
        article = data['article']
        article = apitool.remove_empty_lines(article) # 빈라인 삭제

        prompt = f'아래 문장을 500자 이내로 요약해줘.\n\n{article}'
        print('-> prompt: ' + prompt)
        
        format = '''
            {
            "res": "요약된 문장"
            }
        '''
        response = apitool.answer('너는 요약 시스템이야', prompt, format)
        print('-> response:', response)
        
        return response
    
    except Exception as e:
        # 내부 서버 오류
        return JSONResponse(
            status_code=500,
            content={"error": "서버 내부 오류가 발생했습니다.", "detail": str(e)}
        )        

@app.post("/emotion")
async def emotion_proc(request: Request):
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

        article = data['article']
        article = apitool.remove_empty_lines(article)

        # prompt = f'아래 뉴스가 호재인지 악재인지 알려줘. 호재: 1, 악재: 0\n\n{article}'
        prompt = f'아래 글이 긍정인지 부정인지 알려줘. 긍정: 1, 부정: 0\n\n{article}'
        print('-> prompt: ' + prompt)
        
        format = '''
            {
            "res": "1 또는 0"
            }
        '''
        response = apitool.answer('너는 댓글 긍정, 부정 판단 시스템이야', prompt, format)
        print('-> response:', response)
        
        return response
    
    except Exception as e:
        # 내부 서버 오류
        return JSONResponse(
            status_code=500,
            content={"error": "서버 내부 오류가 발생했습니다.", "detail": str(e)}
        )
        
@app.post("/member_img")
async def member_img_proc(request: Request):
    
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

        prompt = data['prompt']
        prompt = apitool.remove_empty_lines(prompt)

        if os.path.exists('C:/kd/deploy/resort/contents/storage') == False:
            os.mkdir('C:/kd/deploy/resort/contents/storage')

        file_name = apitool.imggen_gpt(prompt, save_dir='C:/kd/deploy/resort/contents/storage')

        response = {"file_name": file_name}

        # return json.loads(response) # str -> json(dict)
        # print('json.dumps(response):', json.dumps(response))
        # json.dumps(response): {"file_name": "C:/kd/deploy/resort/contents/storage/20250516183736_599.jpg"}
        
        # return json.dumps(response) # json(dict) -> str
        print('-> response:', response)
        return response # json 형식 출력 ★
        
    except Exception as e:
        # 내부 서버 오류
        return JSONResponse(
            status_code=500,
            content={"error": "서버 내부 오류가 발생했습니다.", "detail": str(e)}
        )    

app_config = {
    'ALLOWED_EXTENSIONS': {'jpg', 'png', 'gif'}
}

def allowed_file(filename):
    return ('.' in filename) and (filename.rsplit('.', 1)[1].lower() in app_config['ALLOWED_EXTENSIONS'])

# 25 M 제한
def allowed_size(size):
    return True if size <= 1024 * 1024 * 25 else False

upload_folder = 'C:/kd/deploy/resort/contents/storage'

@app.post("/flower")  # http://localhost:8000/flower
async def flower_proc(file: UploadFile = File(...)):
    time.sleep(3)  # 3초 중지

    contents = await file.read()
    file_size = len(contents)
    print('-> file_size:', file_size)

    if allowed_size(file_size) == False:
        return JSONResponse(
            status_code=500,
            content={'message': "파일 사이즈가 25M를 넘습니다." + str(file_size / 1024 / 1024) + ' M'}
        )

    if file and allowed_file(file.filename):
        if not os.path.exists(upload_folder):
            os.makedirs(upload_folder)

        print('-> f.filename', file.filename)
        with open(os.path.join(upload_folder, file.filename), "wb") as f:
            f.write(contents)

        return JSONResponse(
            status_code=200,
            content={'message': '파일을 저장했습니다.', 'filename': file.filename}
        )
    else:
        return JSONResponse(
            status_code=500,
            content={'message': '전송 할 수 없는 파일 형식입니다.'}
        )

@app.post("/pizza")  # http://localhost:8000/pizza
async def flower_proc(file: UploadFile = File(...)):
    time.sleep(3)  # 3초 중지

    contents = await file.read()
    file_size = len(contents)
    print('-> file_size:', file_size)

    if allowed_size(file_size) == False:
        return JSONResponse(
            status_code=500,
            content={'message': "파일 사이즈가 25M를 넘습니다." + str(file_size / 1024 / 1024) + ' M'}
        )

    if file and allowed_file(file.filename):
        if not os.path.exists(upload_folder):
            os.makedirs(upload_folder)

        print('-> f.filename', file.filename)
        with open(os.path.join(upload_folder, file.filename), "wb") as f:
            f.write(contents)

        # -------------------------------------------------------------------        
        # Pizza 이미지 검증
        # -------------------------------------------------------------------
        image_path = "C:/kd/deploy/resort/contents/storage/" + file.filename
        print('-> image_path:', image_path) 
        
        client = OpenAI(
            api_key=os.getenv('OPENAI_API_KEY')
        )
        
        base64_image = apitool.encode_image(image_path)

        prompt = "피자 사진이면 1, 그렇지않으면 0을 출력해줘."
        format = '''
            {
            "res": 피자 사진이면 1, 그렇지않으면 0을 출력해줘.
            }
        '''
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {
                    'role': 'system',
                    'content': "피자 이미지 판별 전문가야",
                },
                {
                "role": "user",
                "content": [
                    {
                        "type": "text", 
                        "text": prompt + '\n\n출력 형식(json): ' + format
                    },
                    {
                        "type": "image_url",
                        "image_url": {
                            "url": f"data:image/jpeg;base64,{base64_image}"
                        },
                    },
                ],
                }
            ],
            max_tokens=3000, # 응답 생성시 최대 1000개의 단어 사용
            temperature=0,   # 창의적인 응답여부, 값이 클수록 확률에 기반한 창의적인 응답이 생성됨
            response_format= { "type":"json_object" }
        )

        print(response.choices[0].message.content)
        # {
        #     "res": 1
        # }
        
        print('-> type:', type(response.choices[0].message.content)) # -> type: <class 'str'>
        
        # sw = response.choices[0].message.content['res']  X
        res_data = json.loads(response.choices[0].message.content) # str -> json
        # print('-> res_data:', res_data)
        
        sw = res_data['res']
        print('-> sw:', sw)
        
        if sw == 1:
            return JSONResponse(
                status_code=200,
                content={'message': '파일을 저장했습니다.', 'filename': file.filename}
            )
        else:
            # 파일삭제
            os.remove(image_path);
            
            # "--": 파일 전송 실패    
            return JSONResponse(
                status_code=200,
                content={'message': '저장할 수 없는 파일입니다. 다시 시도해주세요.', 'filename': "--"}
            )          
        # -------------------------------------------------------------------

    else:
        return JSONResponse(
            status_code=500,
            content={'message': '전송 할 수 없는 파일 형식입니다.'}
        )
                
if __name__ == "__main__":
    # uvicorn.run("resort:app", host="121.78.128.17", port=8000, reload=True) # Gabia 할당 불가
    uvicorn.run("resort_auth:app", host="0.0.0.0", port=8000, reload=True)
    
# (base) C:\kd\ws_python\fastapi>activate ai
# (ai) C:\kd\ws_python\fastapi>python resort_auth.py
    