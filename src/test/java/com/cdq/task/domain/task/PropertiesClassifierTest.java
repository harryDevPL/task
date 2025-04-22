package com.cdq.task.domain.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PropertiesClassifierTest {

    PropertiesClassifier classifier = new PropertiesClassifier();

    @Nested
    @DisplayName("When classifying fields")
    class ClassifyFieldTests {

        @Test
        @DisplayName("should classify as ADDED when old value is null or blank and new value is not blank")
        void shouldClassifyAsAdded() {
            assertThat(classifier.classifyField(null, "new")).isEqualTo(ClassificationType.ADDED);
            assertThat(classifier.classifyField("", "new")).isEqualTo(ClassificationType.ADDED);
            assertThat(classifier.classifyField(" ", "new")).isEqualTo(ClassificationType.ADDED);
        }

        @Test
        @DisplayName("should classify as DELETED when old value is not blank and new value is blank")
        void shouldClassifyAsDeleted() {
            assertThat(classifier.classifyField("old", null)).isEqualTo(ClassificationType.DELETED);
            assertThat(classifier.classifyField("old", "")).isEqualTo(ClassificationType.DELETED);
            assertThat(classifier.classifyField("old", " ")).isEqualTo(ClassificationType.DELETED);
        }

        @Test
        @DisplayName("should classify as HIGH when both values are blank")
        void shouldClassifyAsHighWhenBothBlank() {
            assertThat(classifier.classifyField("", "")).isEqualTo(ClassificationType.HIGH);
            assertThat(classifier.classifyField(null, null)).isEqualTo(ClassificationType.HIGH);
            assertThat(classifier.classifyField(" ", " ")).isEqualTo(ClassificationType.HIGH);
        }

        @Test
        @DisplayName("should classify as HIGH when similarity > 0.9")
        void shouldClassifyAsHigh() {
            assertThat(classifier.classifyField("ABCD", "ABCD")).isEqualTo(ClassificationType.HIGH);
            assertThat(classifier.classifyField("ABCDEABCDEF", "ABCDEABCDEX")).isEqualTo(ClassificationType.HIGH);
        }

        @Test
        @DisplayName("should classify as MEDIUM when similarity is between 0.4 and 0.9 inclusive")
        void shouldClassifyAsMedium() {
            assertThat(classifier.classifyField("ABCD", "BWD")).isEqualTo(ClassificationType.MEDIUM);
            assertThat(classifier.classifyField("ABCDEFG", "CFG")).isEqualTo(ClassificationType.MEDIUM);
            assertThat(classifier.classifyField("ABCABC", "ABC")).isEqualTo(ClassificationType.MEDIUM);
        }

        @Test
        @DisplayName("should classify as LOW when similarity < 0.4")
        void shouldClassifyAsLow() {
            assertThat(classifier.classifyField("ABCD", "BCD")).isEqualTo(ClassificationType.MEDIUM);
            assertThat(classifier.classifyField("ABCDEFGH", "TDD")).isEqualTo(ClassificationType.LOW);
        }
    }
}