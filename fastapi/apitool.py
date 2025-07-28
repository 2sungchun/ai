# tool.py 모듈
import os
import time
import json
import random
import requests  # web 접속
from PIL import Image # 파이썬 이미지 처리, pip install pillow
from io import BytesIO # byte 입출력
import matplotlib.pyplot as plt # 이미지 출력
from datetime import datetime
import base64

# import openai  # 0.28.0
from openai import OpenAI

# openai.api_key='키를 직접 지정하는 경우(권장 아님)' 
# os.environ['OPENAI_API_KEY'] = '키를 직접 지정하는 경우(권장 아님)'

client = OpenAI(
  api_key=os.getenv('OPENAI_API_KEY')
)

# OpenAI API 사용함수
# role: GPT 역활  예) 너는 문화 해설가야
# prompt: 질문 메시지
# format='': 출력 세부 형식, 파라미터 전달이 안되면 아무 값도 사용하지 않는다는 선언
# llm='gpt-4.1-nano, gpt-4.1-mini'
# output='json': 출력 형식
def answer(role, prompt, format, llm='gpt-4.1-nano', output='json'):
    
    if output.lower() == 'json': # 출력 형식이 json인 경우
      response = client.chat.completions.create(
          model=llm,
          messages=[
              {
                  'role': 'system',
                  'content': role
              },
              {
                  'role': 'user',
                  'content': prompt + '\n\n출력 형식(json): ' + format
              }
          ],
          max_tokens=3000, # 응답 생성시 최대 1000개의 단어 사용
          temperature=0,   # 창의적인 응답여부, 값이 클수록 확률에 기반한 창의적인 응답이 생성됨
          response_format= { "type":"json_object" }
      )
    else: # 출력 형식이 json이 아닌 경우
      response = client.chat.completions.create(
          model=llm,
          messages=[
              {
                  'role': 'system',
                  'content': role
              },
              {
                  'role': 'user',
                  'content': prompt
              }
          ],
          n=1,             # 응답수, 다양한 응답 생성 가능
          max_tokens=3000, # 응답 생성시 최대 1000개의 단어 사용
          temperature=0    # 창의적인 응답여부, 값이 클수록 확률에 기반한 창의적인 응답이 생성됨
      )
   
    return json.loads(response.choices[0].message.content) # str -> json
  
# 문자열을 줄바꿈 기준으로 분리하여, 빈 라인을 제거하고, 문장들로 이루어진 리스트 생성
def remove_empty_lines(text):
    lines = [line for line in text.splitlines() if line.strip()]
    # print('-> lines:', lines)
    # print('-' * 80)
    # 문장들을 다시 합쳐서 하나의 문자열로 반환
    result = '\n'.join(lines)
    return result
  
# DBMS connection 생성
# def connection():
#     conn = cx_Oracle.connect('kd/69017000@3.34.236.207:1521/XE')
#     cursor = conn.cursor() # SQL 실행 객체 생성
   
#     return conn, cursor

# save_dir: 생성된 이미지 저장 폴더
def imggen(prompt, model="dall-e-3", size="1024x1024", quality="standard", save_dir='./static/storage'):
    response = client.images.generate(
        prompt=prompt,
        model=model,
        size=size,
        quality=quality,
    )
    
    image_url = response.data[0].url
    print(image_url)
    
    # URL에서 이미지를 가져옴
    response = requests.get(image_url)

    # 응답이 성공적인지 확인, 디버깅시 사용
    # if response.status_code == 200:
    #     # 이미지 데이터를 바이트로 읽어옴
    #     image_data = Image.open(BytesIO(response.content))

    #     # 이미지 표시
    #     plt.imshow(image_data)
    #     plt.axis('off')  # 축을 표시하지 않도록 설정
    #     plt.show()
    # else:
    #     print("이미지를 가져오는데 문제가 발생했습니다.")
    
    # 이미지 저장
    # --------------------------------------------------------------------------------------
    # 현재 시간을 가져옴
    now = datetime.now()

    # '년월일시분초' 형식의 문자열 생성
    date_time_string = now.strftime("%Y%m%d%H%M%S")

    # 1부터 1000까지의 난수 생성
    random_number = random.randint(1, 1000)

    if os.path.exists(save_dir) == False: # 폴더 없으면 폴더 생성
        os.mkdir(save_dir)

    # 파일명 생성 (년월일시분초_난수.txt 형식)
    file_name = f"{save_dir}/{date_time_string}_{random_number}.jpg"

    # 응답이 성공적인지 확인
    if response.status_code == 200:
        # 이미지 데이터를 파일로 저장
        with open(file_name, "wb") as file:
            file.write(response.content)
            
        print("이미지가 성공적으로 저장되었습니다.")
        
    else:
        print("이미지를 가져오는데 문제가 발생했습니다.")
        
    return file_name # 생성되어 저장된 파일명 리턴

# save_dir: 생성된 이미지 저장 폴더
def imggen_gpt(prompt, model="gpt-image-1", size="1024x1024", quality="medium", save_dir='./static/storage'):
    response = client.images.generate(
        prompt=prompt,
        model=model,
        size=size,
        quality=quality,
    )
    
    image_base64 = response.data[0].b64_json
    image_bytes = base64.b64decode(image_base64)
    
    # 이미지 저장
    # --------------------------------------------------------------------------------------
    # 현재 시간을 가져옴
    now = datetime.now()

    # '년월일시분초' 형식의 문자열 생성
    date_time_string = now.strftime("%Y%m%d%H%M%S")

    # 1부터 1000까지의 난수 생성
    random_number = random.randint(1, 1000)

    if os.path.exists(save_dir) == False: # 폴더 없으면 폴더 생성
        os.mkdir(save_dir)

    # 파일명 생성 (년월일시분초_난수.txt 형식)
    file_name = f"{save_dir}/{date_time_string}_{random_number}.jpg"
   
    # 이미지 저장    
    with open(file_name, "wb") as f:
      f.write(image_bytes)

    # 이미지 출력, 디버깅시 사용
    # image_data = Image.open(file_name)  # 이미지 데이터를 바이트로 읽어옴

    # plt.imshow(image_data)  # 이미지 표시
    # plt.axis('off')  # 축을 표시하지 않도록 설정
    # plt.show()
    
    print('-> file_name:', file_name)
        
    return file_name # 생성되어 저장된 파일명 리턴
    
# image -> Text
def encode_image(image_path):
  with open(image_path, "rb") as image_file:
    return base64.b64encode(image_file.read()).decode('utf-8')
        