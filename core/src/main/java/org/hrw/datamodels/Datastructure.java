package org.hrw.datamodels;

/**
 * Marker interface for data objects that can be persisted in the database.
 *
 * <p>Implementations must provide their attribute (column) names as a
 * comma-separated string, which is used by the database layer to dynamically
 * create SQL {@code INSERT} statements.</p>
 */
public interface Datastructure {
    /**
     * Returns the comma-separated list of attribute names corresponding to the
     * database columns of this data structure.
     *
     * @return column name list used for SQL insert operations and hashing
     */
    String getAttributeNames();
}
