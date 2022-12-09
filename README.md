# spartablog

## UseCase
![spartablog_usecase](https://user-images.githubusercontent.com/117057544/206616727-d938bc9c-9cc8-4b98-ac99-f11992d667a4.jpg)

## API 명세서
|Method|URL|Request|Response|
|:---|:---|:---|:---|
|POST|posts|{"username":"username",  "title":"title",  "password":"password",  "contents":"contents"}|{"id":1,  "username":"username",  "title":"title",  "modifiedAT":"2022-12-07T12:43:01.226062”}|
|GET|posts|-|{"id":1,  "username":"username",  "title":"title",  "modifiedAT":"2022-12-07T12:43:01.226062”}|
|GET|posts/{id}|-|{"id":1,  "username":"username",  "title":"title",  "contents:"contents",  "modifiedAT":"2022-12-07T12:43:01.226062”}|
|PUT|posts/{id}|{"username":"username",  "password":"password",  "contents":"contents2"}|{"id":1,  "username":"username",  "title":"title",  "contents":"contents2",  "modifiedAT":"2022-12-08T12:43:01.226062”}|
|DELETE|posts/{id}|{"password":"password"}|{"success": true}|
