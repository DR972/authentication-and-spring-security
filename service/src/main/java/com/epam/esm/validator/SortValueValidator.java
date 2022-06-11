package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.SortValueException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class {@code SortValueValidator} provides method to Sorting Type validate.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Component
public class SortValueValidator {
    private static final String TAGS = "tags";
    private static final String ORDERS = "customerOrders";
    private static final String ID = "id";

    /**
     * This method validate type of sort.
     *
     * @param types List<String> types
     */
    @SneakyThrows
    public void validateSortType(List<String> types) {
        List<String> fields = Arrays.stream(GiftCertificate.class.getDeclaredFields()).map(Field::getName)
                .filter(field -> (!field.equals(TAGS)) && (!field.equals(ORDERS))).collect(Collectors.toList());
        List<String> decodedTypes = new ArrayList<>();
        for (String type : types) {
            decodedTypes.add(URLEncoder.encode(type, StandardCharsets.UTF_8.name()));
        }
        List<String> illegalTypes = decodedTypes.stream()
                .map(type -> type.startsWith("-") ? type.substring(1) : type)
                .filter(type -> (!fields.contains(type)) && (!type.equals(ID)))
                .collect(Collectors.toList());
        if (!illegalTypes.isEmpty()) {
            throw new SortValueException("ex.sortType", String.join(", ", illegalTypes));
        }
    }
}
