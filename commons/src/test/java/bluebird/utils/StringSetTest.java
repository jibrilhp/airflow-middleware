package jibs.utils;

//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.TreeSet;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author gusto
// */
//class StringSetTest {
//
//    private static final Logger L = LoggerFactory.getLogger(StringSetTest.class);
//
//    /**
//     * Test of findNew method, of class StringSet.
//     */
//    @Test
//    void testFindNew() {
//
//        StringSet comparator = new StringSet(Arrays.asList("AA", "BB", "CC", "DD", "FF", "GG"));
//        Set<String> newSet = comparator.findNew(Arrays.asList("AA", "BB", "CC", "DD", "EE", "AA"));
//        for (String v : newSet) {
//            L.info("NEW: {}", v);
//        }
//        assertArrayEquals(new TreeSet<>(newSet).toArray(), new TreeSet<>(Arrays.asList("EE")).toArray());
//    }
//
//    /**
//     * Test of findDeleted method, of class StringSet.
//     */
//    @Test
//    void testFindDeleted() {
//        StringSet comparator = new StringSet(Arrays.asList("AA", "BB", "CC", "DD", "FF", "GG"));
//        Set<String> deletedSet = comparator.findDeleted(Arrays.asList("AA", "BB", "CC", "DD", "EE", "AA"));
//        for (String v : deletedSet) {
//            L.info("DELETED: {}", v);
//        }
//        assertArrayEquals(new TreeSet<>(deletedSet).toArray(), new TreeSet<>(Arrays.asList("FF", "GG")).toArray());
//    }
//
//}
