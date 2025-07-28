from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
import uvicorn
import apitool

app = FastAPI()

# 2) CORS 설정 (브라우저 스크립트용)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def test():
    return {"resort": "Spring Boot resort_v7sbm3c"}

@app.post('/translator')
async def translator_proc(request: Request):
    data = await request.json()
    sentence = data['sentence']
    language = data['language']
    age = data['age']
    prompt = f'아래 문장을 {age}살 수준의 {language}로 존댓말로 간결히 번역해줘.\n\n{sentence}'
    fmt = '{ "res": "번역된 문장" }'
    response = apitool.answer('너는 번역가야', prompt, fmt, llm='gpt-4.1-mini')
    return response

@app.post('/movie')
async def movie_proc(request: Request):
    data = await request.json()
    movie = list(map(int, data['movie'].split(",")))
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

@app.post('/genre')
async def genre_proc(request: Request):
    data = await request.json()
    movie = list(map(int, data['movie'].split(",")))
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

if __name__ == "__main__":
    # uvicorn.run("resort:app", host="121.78.128.17", port=8000, reload=True) # Gabia 할당 불가
    uvicorn.run("resort:app", host="0.0.0.0", port=8000, reload=True)
    
    