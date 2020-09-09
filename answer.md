# DI について

## DI (Dependency Injection) とは
- Dependency = 依存
```java
public class RecipeService {
    private RecipeRepository recipeRepository = new RecipeRepository();

    public void create() {
        recipeRepository.create();
    }
}
```
以上の例の様に RecipeService クラスは RecipeRepository クラスを必要としている。つまり RecipeService クラスは RecipeRepository に依存している状態。

- Injection = 注入

注入とは依存するクラスのインスタンスを外部から渡す事を指す。

```java
public class RecipeService {
    private RecipeRepository recipeRepository = new RecipeRepository();
}
```
以上の例では recipeRepository は宣言と同時に初期化されている。これをコンストラクタを用いて注入すると以下の様になる。

```java
public class RecipeService {
    private RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository  = recipeRepository;
    }
}
```

以上の様に Inversion of Control という具象クラスが何になるかは外部で決めるようにするというパターンを DI によって実現することができる。
上記の例で言うと RecipeService は　RecipeRepository を保持している為、処理の流れとしては RecipeService -> RecipeRepository となるが、RecipeRepository を生成してそれを RecipeService に注入するような設計にする事で制御を反転させ、RecipeRepository -> RecipeService という処理の流れになり, RecipeRepository 型のインスタンスの具体的実装は外部で決める事ができる。

## DI の利点

外部から RecipeRepository のインスタンスを渡す作りにする事で RecipeRepository の実装が何なのかを隠す事もでき、さらに言えばそのサブクラスを渡す事も可能となる。さらにテストの際に RecipeRepository クラスを Mock に置き換える事で他クラスに依存しない単体テストを作成する事も可能となる。
例としては以下に示すように
```java
public class RecipeService {
    private RecipeRepository recipeRepository = new RecipeRepository();
}
```
RecipeService 自身で RecipeRepository をインスタンス化する様な設計となっているクラスの単体テストを行う場合、RecipeRepositoryの実装に依存しないテストを行いたいため、 RecipeRepository を Mock する必要があるが、その為に以上のコードを書き替えて
```java
public class RecipeService {
    private RecipeRepository recipeRepository = new MockRecipeRepository();
    //private RecipeRepository recipeRepository = new RecipeRepository();
}
```
上記の様にテストの度にコメントアウトで切り替える必要が出てしまう。その際に DI を用いて RecipeRepository 型のクラスのインスタンを渡す様な設計とすればそのような事にはならない。テストの際以外にも例えばログの出力を行うラッパークラスを作成してそちらのインスタンスと差し替えるといった事も可能になる。

## DI コンテナについて
DI コンテナとは、アプリケーションに DI 機能を提供するフレームワークである。DI コンテナ無しで DI を行う場合、以下のように
```java
RecipeRepository recipeRepository = new RecipeRepository();
RecipeService recipeService = new RecipeService(recipeRepository);
```
というように RecipeService クラスのインスタンスを構築する為に先に RecipeRepository のインスタンスを構築する必要がある。しかし例としてSpring の DI コンテナであれば @Autowired のアノテーションを使用することで DI コンテナ側でインスタンスを生成してくれる。

## インジェクション方式
多くの DI コンテナでは次の3つのインジェクション方式が用いられる

- コンストラクタインジェクション
- フィールドインジェクション
- セッターインジェクション

コンストラクタインジェクションでは上記で用いている依存コンポーネントをコンストラクタ引数へ渡す方式である。フィールドインジェクションはインスタンス生成後に依存コンポーネントをフィールドに直接セットする方式。 Spring FrameWork の場合は @Autowired のアノテーションで実装される。セッターインジェクションはインスタンス生成後にセッターメソッドを経由して依存コンポーネントを渡す方式である。Spring FrameWork の場合は @Autowired をセッターメソッドにつけるとセッターインジェクションになる。

## 今回のコーディング試験に置ける DI の実装

@Component、@Service、@Repository、@Controller これらのアノテーションを用いる事によって Spring の DI コンテナに bean としての登録を行う。実際のインジェクションには以下の様な @Autowired のアノテーションを付与してフィールドインジェクションを行なっている。@Autowired が付与された注入先の変数には bean 登録されたものから該当するクラスが注入される。

```java
@Service
public class RecipeServiceImpl implements RecipeService {

  @Autowired
  RecipeRepository recipeRepository;
}
```
