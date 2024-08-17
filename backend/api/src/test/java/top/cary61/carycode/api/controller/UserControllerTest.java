package top.cary61.carycode.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import top.cary61.carycode.commons.entity.Result;
import javax.annotation.Resource;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Resource
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeAll
    void beforeAll() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getUser() throws Exception {
        var request = get("/user")
                .param("id", "1");
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void getNonExistedUser() throws Exception {
        var request = get("/user")
                .param("id", "-1");
        String json = objectMapper.writeValueAsString(Result.fail("获取用户信息失败"));
        mockMvc.perform(request)
                .andExpect(content().json(json));
    }

}
