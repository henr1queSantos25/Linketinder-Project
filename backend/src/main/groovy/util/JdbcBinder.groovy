package util

import java.sql.PreparedStatement
import java.sql.Types
import java.time.LocalDate

class JdbcBinder {

    static void bind(PreparedStatement stmt, Object... params) {
        params.eachWithIndex { value, i ->
            int idx = i + 1
            if (value == null) {
                stmt.setNull(idx, Types.NULL)
            } else if (value instanceof LocalDate) {
                stmt.setDate(idx, java.sql.Date.valueOf(value))
            } else {
                stmt.setObject(idx, value)
            }
        }
    }
}