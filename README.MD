When you want to properly test your code, 
you mostly always hassle with initializing fitting test data for your test.

This process becomes way easier by utilizing the builder pattern to produce 
`TestBuilder` classes. 

Lets say your program has a class called `Person` which looks like this:

```java
import com.niklas.TestBuilder;
import lombok.Setter;

@TestBuilder
@Setter
public class Person {
    String firstName;
    String lastName;
    int age;
    double money;
}
```

By setting the `@TestBuilder` annotation a dedicated `PersonTestBuilder` class
will be generated automatically for you. In order to actually experience the convience
of this process you need to utilize **sensible default values** in order to test
properly. These default values can be set in a dedicated `Person.yaml` file which needs to
be put inside the `resources/TestBuilders` folder. `Person.yaml` could look like this: 

```yaml
firstName: "John"
lastName: "Doe"
age: 25
money: 26.25
```

This will generate the following `PersonTestBuilder` class: 
```java
public class PersonTestBuilder {
  private String firstName = "John";

  private String lastName = "Doe";

  private int age = 25;

  private double money = 26.25;

  public static PersonTestBuilder defaults() {
    return new PersonTestBuilder();
  }

  public Person build() {
    Person person = new Person();
    person.setFirstName(firstName);
    person.setLastName(lastName);
    person.setAge(age);
    person.setMoney(money);
    return person;
  }

  public PersonTestBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public PersonTestBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public PersonTestBuilder withAge(int age) {
    this.age = age;
    return this;
  }

  public PersonTestBuilder withMoney(double money) {
    this.money = money;
    return this;
  }
}
```

For now the generator works for primitive fields only. Complex objects will be set to `null`.

Now lets say you want a feature inside your application which logic depends on the value of `age`. 
_(E.g. correct age verification)_

With the generated builder this becomes trivial: 
```java
Person underaged = PersonTestBuilder.defaults().withAge(16).build(); 
```
You don't have to concern yourself about failing code in other places since the sensible default is already set
and doesn't need to be dealt with. 
