# RESTful な API 実装

RESTful Web services は軽量で、スケーラビリティーに優れ、メンテナンスがしやすく、Web アプリケーションを作成する際に一般的に使用されるものです。

## ザ・ミッション
RESTful な Web サービスの設計原則 を満たす、レシピ API サーバーを実装・公開してください。

# 実装の詳細
## 概要

下記のエンドポイントを実装してください:

- POST /recipes -> レシピを作成
- GET /recipes -> 全レシピ一覧を返す
- GET /recipes/{id} -> 指定レシピ一つを返す
- PATCH /recipes/{id} -> 指定レシピを更新
- DELETE /recipes/{id} -> 指定レシピの削除

レスポンスは全て JSON 形式で返してください。

その他明示していない詳細は
[Web API: The Good Parts](https://www.amazon.co.jp/exec/obidos/ASIN/4873116864/shohei0823-22)
に準ずる実装をしてください。

既ににデータベースのスキーマが準備されています。  
sql/create.sql を使用し、データベースを作成してください。  

Heroku を使用して、アプリをサーバー上にデプロイしてください。  
アプリをデプロイした後、アプリの URL をプロジェクトに用意された JUnit の RestTest の `RestAssured.baseURI` に保存してください。  

```
@BeforeAll
void setupAll() {
  RestAssured.baseURI = "http://localhost:8080/";
}
```

RestTest をテスト実行し、実装物が要求を満たしているか確認ください。  

# 基本仕様
## POST /recipes エンドポイント

recipe を新規作成します。

### 期待する request 形式: 

```
POST /recipes

Body フィールド:  
title, making_time, serves, ingredients, cost  
上記パラメーターは全て必須です。  
sql/create.sql にて各プロパティの説明を確認できます。  
```

### 期待する response 形式:

** 成功 response: **

- HTTP Status: 201

```
{
  "message": "Recipe successfully created!",
  "recipe": [
    {
      "title": "トマトスープ",
      "making_time": "15分",
      "serves": "5人",
      "ingredients": "玉ねぎ, トマト, スパイス, 水",
      "cost": "450"
    }
  ]
}

```

** 失敗 response: **

- HTTP Status: 400

```
{
 "message": "Recipe creation failed!",
 "required": "title, making_time, serves, ingredients, cost"
}
```

## GET /recipes エンドポイント

データベースの全てのレシピをを返します。

### 期待する request 形式: 

```
GET /recipes
```

### 期待する response 形式:

- HTTP Status: 200

```
{
  "recipes": [
    {
      "id": 1,
      "title": "チキンカレー",
      "making_time": "45分",
      "serves": "4人",
      "ingredients": "玉ねぎ,肉,スパイス",
      "cost": "1000"
    },
    {
      "id": 2,
      "title": "オムライス",
      "making_time": "30分",
      "serves": "2人",
      "ingredients": "玉ねぎ,卵,スパイス,醤油",
      "cost": "700"
    },
    {
      "id": 3,
      "title": "トマトスープ",
      "making_time": "15分",
      "serves": "5人",
      "ingredients": "玉ねぎ, トマト, スパイス, 水",
      "cost": "450"
    }
  ]
}
```

## GET /recipes/{id} エンドポイント

指定 id のレシピのみを返します。

### 期待する request 形式: 

```
GET /recipes/1
```

### 期待する response 形式:

- HTTP Status: 200

```
{
  "message": "Recipe details by id",
  "recipe": [
    {
      "title": "チキンカレー",
      "making_time": "45分",
      "serves": "4人",
      "ingredients": "玉ねぎ,肉,スパイス",
      "cost": "1000"
    }
  ]
}
```

** 失敗 response (指定 id のレシピが存在しない場合): **

- HTTP Status: 404

```
{
  "message":"No Recipe found"
}
```



## PATCH /recipes/{id} エンドポイント

指定 id のレシピを更新し、更新したレシピを返します。

### 期待する request 形式: 

```
PATCH /recipes/{id}

Body フィールド:
title, making_time, serves, ingredients, cost
```

### 期待する response 形式:

- HTTP Status: 200

```
{
  "message": "Recipe successfully updated!",
  "recipe": [
    {
      "title": "トマトスープレシピ",
      "making_time": "15分",
      "serves": "5人",
      "ingredients": "玉ねぎ, トマト, スパイス, 水",
      "cost": "450"
    }
  ]
}
```

** 失敗 response (指定 id のレシピが存在しない場合): **

- HTTP Status: 404

```
{
  "message":"No Recipe found"
}
```



## DELETE /recipes/{id} エンドポイント

指定 id のレシピを削除します。

### 期待する request 形式:

```
DELETE /recipes/1
```

### 期待する response 形式:

** 成功 response: **

- HTTP Status: 204

** 失敗 response (指定 id のレシピが存在しない場合): **

- HTTP Status: 404

```
{
  "message":"No Recipe found"
}
```
