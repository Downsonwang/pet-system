package com.pet.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import com.pet.project.Entity.UserT;
import com.pet.project.Entity.UserRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllUsers() {
        ResponseEntity<UserT[]> response = restTemplate.getForEntity("/api/users", UserT[].class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void testCreateUser() {
        UserT user = new UserT();
        user.setUsername("testuser");
        user.setPassword("123456");
        user.setRole(UserRole.CUSTOMER);

        ResponseEntity<UserT> response = restTemplate.postForEntity("/api/users", user, UserT.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
    }
    @Test
    void testCreateAndVerifyUser() {
        // 1. 创建用户对象
        UserT user = new UserT();
        user.setUsername("testuser");
        user.setPassword("123456");
        user.setRole(UserRole.CUSTOMER);

        // 2. 发送POST请求创建用户
        ResponseEntity<UserT> createResponse = restTemplate.postForEntity("/api/users", user, UserT.class);
        assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();

        // 3. 发送GET请求获取刚创建的用户
        ResponseEntity<UserT[]> getAllResponse = restTemplate.getForEntity("/api/users", UserT[].class);
        assertThat(getAllResponse.getStatusCode().is2xxSuccessful()).isTrue();

        // 4. 在返回的用户列表中查找刚创建的用户
        UserT[] users = getAllResponse.getBody();
        boolean found = false;
        for (UserT u : users) {
            if ("testuser".equals(u.getUsername())) {
                found = true;
                assertThat(u.getRole()).isEqualTo(UserRole.CUSTOMER);
                break;
            }
        }
        assertThat(found).isTrue();
    }
}