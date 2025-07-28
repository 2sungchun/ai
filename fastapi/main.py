from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
import uvicorn
import apitool

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # ★ 모든 ip 허용
    allow_credentials=True,
    allow_methods=["*"],     # 필요에 따라 ["GET","POST"] 등으로 제한 가능
    allow_headers=["*"],     # 필요에 따라 ["Authorization","Content-Type"] 등으로 제한 가능
)

@app.get("/")  # http://localhost:8000
def hello():
    return {"hello": "FastAPI"}

if __name__ == "__main__":  # python main.py
    # main.py 파일명:app 변수
    # host="0.0.0.0": 접속 가능 컴퓨터
    # reload=True: 소스 변경시 자동 재시작
    uvicorn.run("main:app", host="0.0.0.0", reload=True)
    
'''
(base) C:\kd\ws_python\fastapi>activate ai
(ai) C:\kd\ws_python\fastapi>python main.py
'''
