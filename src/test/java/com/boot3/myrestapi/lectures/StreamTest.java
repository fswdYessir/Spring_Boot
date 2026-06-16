package com.boot3.myrestapi.lectures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class StreamTest {

    /*
        Stream 의 map() 과 flatMap의 차이점 이해
     */
    @Test
    public void transformUsingStream(){
        List<MyCustomer> customers = List.of(
                new MyCustomer(101, "john", "john@gmail.com", Arrays.asList("397937955", "21654725")),
                new MyCustomer(102, "smith", "smith@gmail.com", Arrays.asList("89563865", "2487238947")),
                new MyCustomer(103, "peter", "peter@gmail.com", Arrays.asList("38946328654", "3286487236")),
                new MyCustomer(104, "kely", "kely@gmail.com", Arrays.asList("389246829364", "948609467"))
        );

        //email 주소 목록 List<String>
        List<String> emailList = customers.stream()  //Stream<MyCustomer> -> Stream<MyCustomer>
                .map(cust -> cust.getEmail()) //Stream<String>
                .toList();//List<String>
        //Iterable의 forEach()
//        emailList.forEach(String email -> System.out.println(email));
        emailList.forEach(System.out::println);

        customers.stream()
                .map(MyCustomer::getEmail)
                .collect(toList())
                .forEach(System.out::println);

        //map() : <R> Stream<R> map(Function<? super T,? extends R> mapper)
        List<List<String>> phoneList = customers.stream() //List<MyCustomer> -> Stream<Customer>
                .map(cust -> cust.getPhoneNumbers()) //Stream<List<String>>
                .collect(toList()); //List<List<String>>
        System.out.println("phoneList = " + phoneList);


        //flatMap : <R> Stream<R> flatMap(Function<? super T,? extends Stream<? extends R>> mapper)
        List<String> phoneList2 = customers.stream() //Stream<Customer>
                .flatMap(customer -> customer.getPhoneNumbers().stream())   //Stream<String>
                .collect(toList()); //List<String>
        System.out.println("phoneList2 = " + phoneList2);

    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MyCustomer {
    private int id;
    private String name;
    private String email;
    private List<String> phoneNumbers;

    public MyCustomer(String name, String email) {
        this.name = name;
        this.email = email;
    }
}