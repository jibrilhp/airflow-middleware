package jibs.utils;
//
//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author gusto
// */
//@MicronautTest
//public class CaseUtilsTest {
//
//    private static final Logger L = LoggerFactory.getLogger(CaseUtilsTest.class);
//
//    @Test
//    public void testToSnakeCase() {
//        String input, expected, result;
//
//        input = "toSnakeCase";
//        expected = "to_snake_case";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "toSnake_Case";
//        expected = "to_snake_case";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "toSNAKE_Case";
//        expected = "to_snake_case";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "ToSNAKE_Case";
//        expected = "to_snake_case";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "toSNAKECase";
//        expected = "to_snake_case";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "ToSNAKECase";
//        expected = "to_snake_case";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "InterfaceA";
//        expected = "interface_a";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "InterfaceALL";
//        expected = "interface_all";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "InterfaceFI";
//        expected = "interface_fi";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "Interface_FI";
//        expected = "interface_fi";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "InterfaceFINAL";
//        expected = "interface_final";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "Status_Datang";
//        expected = "status_datang";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//
//        input = "ADM_Tabungan_Pengemudi_2";
//        expected = "adm_tabungan_pengemudi_2";
//        result = CaseUtils.toSnakeCase(input);
//        L.info("{}: {} <=> {}", input, expected, result);
//        assertEquals(expected, result);
//    }
//
//}
