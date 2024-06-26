// 현재 URL의 쿼리 스트링을 가져오기
const queryString = window.location.search;

// URLSearchParams 객체를 사용하여 쿼리 스트링 파싱
const urlParams = new URLSearchParams(queryString);

// 특정 매개변수 값 가져오기
const date = urlParams.get('date');
const user = urlParams.get('user');

const diary = document.getElementById("diary");

const http = new XMLHttpRequest();

http.open('GET', url + `api/DB/diary?date=${date}&user=${user}`);
http.send();
http.onload = () => {
    if( http.status === 200 ) {
        console.log(http.response);
        let content = document.createElement("p");

        if (http.response != "")
            content.innerText = http.response;
        else
            content.innerText = "Wrong Access!!";

        diary.appendChild(content);

    } else {
        console.error("Error", http.status, http.statusText);
    }
};