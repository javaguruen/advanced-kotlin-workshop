# Exercises part 2
The goal of this exercise is to practice implementing a custom domain type/class
where the business logic for valid values are implemented in the custom type 
not in a separate validator. Thus we are guaranteed to alway have instances with
valid values. you will also use the arrow library's Validated when validating the
values before instantiation.

## Before you start
In the directory `exercise02/` there is an application that are implemented the
traditional way with a simple rest controller, mapping from api model to domain model and, a servie to "do the things" and a repository to persist Users.

Run `mvn clean install` before you start.

## Add dependency to Arrow
Add dependency to the `pom.xml` file:
```xml
<dependency>
  <groupId>io.arrow-kt</groupId>
  <artifactId>arrow-core-jvm</artifactId>
  <version>1.1.2</version>
</dependency>
```
Feel free to replace the version number with any newer version if exists.

## Class for ValidationError
Create a data class ValidationError that takes a String value as parameter

In the same file as the ValidationError create a `typealias ValidationErrors = NonEmptyList<ValidationError>`

## Custom type for Email
In the `no.advkotlin.exercises02.application.domain.User` class, an email address
is represented as `String`. Implement an inline class Email that has
 - private constructor
 - a factory method creating a new instance

Move relevant validation from UserValidator to the inline class.

Factory method should return a Validated<ValidationErrors, Email> depending on the result of the validation.

## Custom type for Username
In the same way as for Email, create an inline class for Username.
Add validation that the username is not an empty string and it's length is less than or equal to 100 characters.

## Change the signature for User (domain)
Change the constructor arguments for the domain User to use the newly created
Email and Username instead of String and String.

## Check for validation errors in Api2DomainMapper
Create "validated" Email and Username instances. Use the shown zip method to either get a `Valid<User>` or `Invalid<ErrorMessages>`. Return the User object if valid or throw Exception with a combined String of all the errors.

## Change UserValidatorTest
Change the tests. UserValidator does not have the validate methods any more.

The easiest way to check both validation rules in each class and the aggregation of error messages is to call `Api2DomainMapper.mapUser()` in the tests.

