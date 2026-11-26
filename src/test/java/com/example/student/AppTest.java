package com.example.student;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void parsesFormBody() {
        Map<String, String> m = App.parseForm("name=Asha+Rao&roll=21CS101&email=a%40b.com&course=BCA");
        assertEquals("Asha Rao", m.get("name"));
        assertEquals("21CS101", m.get("roll"));
        assertEquals("a@b.com", m.get("email"));
        assertEquals("BCA", m.get("course"));
    }

    @Test
    void escapesHtml() {
        assertEquals("&lt;b&gt;hi&lt;/b&gt;", App.escape("<b>hi</b>"));
    }
}
