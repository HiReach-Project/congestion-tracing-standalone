CREATE INDEX point_geom_idx
    ON device_location_history
        USING GIST (location_point);