package Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LocationDAO {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Location> list() {
        String query = "SELECT * FROM locations";

        List<Location> locationList = template.query(query, new RowMapper<Location>() {
            @Override
            public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
                Location location = new Location();
                location.setLocation_id(rs.getInt(1));
                location.setStreet_address(rs.getString(2));
                location.setPostal_code(rs.getString(3));
                location.setCity(rs.getString(4));
                location.setState_province(rs.getString(5));
                location.setCountry_id(rs.getString(6));
                return location;
            }
        });

        return locationList;
    }

    public Location insert(Location location) {
        String query = "insert into locations values (:location_id, :street_address, :postal_code, :city, :state_province," +
                ":country_id)";

        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("location_id", location.getLocation_id())
                .addValue("street_address", location.getStreet_address())
                .addValue("postal_code", location.getPostal_code())
                .addValue("city", location.getCity())
                .addValue("state_province", location.getState_province())
                .addValue("country_id", location.getCountry_id());
        namedParameterJdbcTemplate.update(query,parameterSource,holder);
        return location;

    }

    public void delete(int location_id) {
        String query = "DELETE FROM LOCATIONS WHERE location_id = ?";
        template.update(query, location_id);
    }

    public Location update(int location_id) {
        String query = "SELECT * FROM LOCATIONS WHERE location_id = ?";

        return template.queryForObject(query, new Object[]{location_id}, (rs, rowNum) ->
                new Location(
                        rs.getInt("location_id"),
                        rs.getString("street_address"),
                        rs.getString("postal_code"),
                        rs.getString("city"),
                        rs.getString("state_province"),
                        rs.getString("country_id")
                ));
    }

    public void save(Location location) {
        String query = "UPDATE LOCATIONS SET street_address=:street_address, postal_code=:postal_code, city=:city, state_province=:state_province, country_id=:country_id WHERE location_id=:location_id";
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(location);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
        namedParameterJdbcTemplate.update(query, parameterSource);
    }
}
