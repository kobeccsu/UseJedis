import com.google.gson.Gson;
import dto.Person;
import org.junit.jupiter.api.*;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class ConnectTest {

    List<Person> DUMMY_DATA = Stream.of(
            new Person(1, 2, true),
            new Person(2, 18, false),
            new Person(3, 20, true))
        .collect(Collectors.toList());

    static Jedis jedis;
    @BeforeAll
    static void setUp(){
        //
        jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("setup");
    }

    @AfterAll
    static void tearDown(){
        jedis.close();
        System.out.println("tearDown");
    }

    @Test
    void canConnect(){

        jedis.sadd("zhaohui", "he", "sd");
//        System.out.println("I can get " + jedis("zhaohui"));
        jedis.close();
    }

    @Test
    void shouldSetString(){
        HashMap<String, String> map = new HashMap<>();

        Gson gson = new Gson();

        for (Person person : DUMMY_DATA){
            map.put(String.valueOf(person.getId()), gson.toJson(person));
        }

        jedis.hset("list", map);


    }

    @Test
    void shouldGetTableData(){
        Gson gson = new Gson();

        Person person = gson.fromJson(jedis.hget("list", "1"), Person.class);
        System.out.println(person);

        Assertions.assertTrue(person.isGender());
    }

    @Test
    void shouldSetSomeKeyExpirySpecificTime(){
        // set some key expire
        jedis.expire("list", 10l);
    }

    @Test
    @DisplayName("should get value")
    void shouldGetValueIfKeyNotExists(){
        String jjj = jedis.get("jjj");
        Assertions.assertNull(jjj);
    }

    @Test
    @DisplayName("should test full name scenario")
    void shouldTestFullNameScenario() {
        fail();

    }
}
