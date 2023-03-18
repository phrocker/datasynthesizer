package org.dataguardians.datasynth.query.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * QueryConfiguration represents the configuration options for a database query.
 * It provides methods for setting various options and modifying the behavior of the query.
 *
 * Methods:
 * - setPageSize(int pageSize): Set the page size for paginated results.
 * - setPageNumber(int pageNumber): Set the page number for paginated results.
 * - setSortKey(String sortKey): Set the sort key for the query result.
 * - setSortOrder(SortOrder sortOrder): Set the sort order for the query result.
 * - addFilter(String fieldName, Object value): Add a filter to the query based on a field name and value.
 * - removeFilter(String fieldName): Remove a filter from the query based on its field name.
 *
 * Usage example:
 *
 * QueryConfiguration config = new QueryConfiguration();
 * config.setPageSize(10);
 * config.setPageNumber(1);
 * config.setSortKey("name");
 * config.setSortOrder(SortOrder.ASCENDING);
 * config.addFilter("category", "books");
 * config.addFilter("price", new BigDecimal("20.00"));
 * config.removeFilter("category");
 *
 * This class is immutable and thread-safe.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryConfiguration {

    private List<DataDictionaryDefinition> dataDictionary;

    private QueryType queryType;

    private int count;

    private boolean invalidOnly;

    /**
     * QueryConfiguration represents the configuration options for a database query.
     * It provides methods for setting various options and modifying the behavior of the query.
     *
     * Methods:
     * - setPageSize(int pageSize): Set the page size for paginated results.
     * - setPageNumber(int pageNumber): Set the page number for paginated results.
     * - setSortKey(String sortKey): Set the sort key for the query result.
     * - setSortOrder(SortOrder sortOrder): Set the sort order for the query result.
     * - addFilter(String fieldName, Object value): Add a filter to the query based on a field name and value.
     * - removeFilter(String fieldName): Remove a filter from the query based on its field name.
     *
     * Usage example:
     *
     * QueryConfiguration config = new QueryConfiguration();
     * config.setPageSize(10);
     * config.setPageNumber(1);
     * config.setSortKey("name");
     * config.setSortOrder(SortOrder.ASCENDING);
     * config.addFilter("category", "books");
     * config.addFilter("price", new BigDecimal("20.00"));
     * config.removeFilter("category");
     *
     * This class is immutable and thread-safe.
     */
    @Data
    @Builder
    public static class /**
     * Data dictionary for the query configuration object.
     */
    DataDictionaryDefinition {

        public String fieldName;

        public FieldType type;
    }
}
