# Comparatorとは
ソート条件のルールを定義するインターフェースのこと。  
sort()メソッドに渡すと、ソートするのに必要な回数だけcompare()メソッドを実行してくれる。  
  
# Comparatorの生成
何度も使うソート条件なのであれば、定数で定義しておくとよい。  
下記のように、Comparator型の定数フィールドを宣言する。  
```java
class EmployeeInfo {

    // 氏名昇順（通常の書き方）
    public static final Comparator<EmployeeInfo> BY_NAME = new Comparator<EmployeeInfo>() {
        @Override
        public int compare(EmployeeInfo o1, EmployeeInfo o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    // 生年月日昇順（メソッド参照で簡潔に書く場合）
    public static final Comparator<EmployeeInfo> BY_BIRTH_DATE =
        Comparator.comparing(EmployeeInfo::getBirthDate);

    // 他のComparatorも同様に定義する
}
```
  
# sort()メソッドでComparatorを呼び出す
Listのsort()メソッドを呼び出し、使いたいComparatorを引数として渡す。  
```java
employeeList.sort(EmployeeInfo.BY_NAME);
```
  
# sort()メソッドの挙動
Listのsort()メソッドは、呼び出し元のListが直接ソートされるvoid型のメソッド。  
ソート後のListを生成して返すというわけではないので、  
もし元のListを保持したいのであれば、ソート結果表示用のListを用意してそちらだけソートする。  
```java
List<EmployeeInfo> sortedList = new ArrayList<>(employeeList);
sortedList.sort(EmployeeInfo.BY_NAME);
```
  
# 参考サイト
[JavaのComparatorクラスの使い方を現役エンジニアが解説【初心者向け】](https://magazine.techacademy.jp/magazine/34841)
