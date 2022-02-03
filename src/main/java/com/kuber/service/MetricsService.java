package com.kuber.service;

import com.kuber.model.Dictionary;
import com.kuber.model.MetricsResponse;
import com.kuber.model.RawMaterialDictionary;
import com.kuber.service.mapper.MetricsResponseRowMapper;
import com.kuber.service.mapper.RawMaterialDictionaryRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetricsService {
    private static Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

    private static final String GET_AVAILABLE_STOCK_AND_PENDING_ORDERS = "SELECT i.sum - oc.sum AS availableStock, op.sum AS pendingOrders, p.Product_Id as productId, p.Product_Type AS title FROM \n" +
            "((SELECT Product_Id, sum(Quantity) AS sum FROM Inventory GROUP BY 1) i \n" +
            " INNER JOIN (SELECT Product_Id, sum(Quantity) AS sum FROM Orders WHERE Status=1 GROUP BY 1) oc \n" +
            " ON i.Product_Id = oc.Product_Id \n" +
            " LEFT JOIN (SELECT Product_Id, sum(Quantity) AS sum FROM Orders WHERE Status=0 GROUP BY 1) op\n" +
            " ON i.Product_Id = op.Product_Id) \n" +
            " LEFT JOIN Product p ON i.Product_Id = p.Product_Id GROUP BY p.Product_Type;";

    private static final String GET_AVAILABLE_RAW_MATERIAL_LIST_BY_PRODUCT_ID = "  Select rm.Raw_Material_Name as 'label',\n" +
            " CASE  \n" +
            " when (rmp.Product_Id = 1 or rmp.Product_Id = 2 or rmp.Product_Id = 3 or rmp.Product_Id = 4) and rmp.Raw_Material_Id =1 then ( rmp.sum - i.sum ) \n" +
            " when rmp.Product_Id = 1 and (rmp.Raw_Material_Id=2 or rmp.Raw_Material_Id =3 or rmp.Raw_Material_Id =4) then (rmp.sum - (i.sum *48) )\n" +
            " when rmp.Product_Id = 2 and (rmp.Raw_Material_Id=2 or rmp.Raw_Material_Id =3 or rmp.Raw_Material_Id =4) then (rmp.sum - (i.sum *24 ))\n" +
            " when rmp.Product_Id = 3 and (rmp.Raw_Material_Id=2 or rmp.Raw_Material_Id =3 or rmp.Raw_Material_Id =4) then ( rmp.sum - (i.sum*12 ))\n" +
            " when rmp.Product_Id = 4 and (rmp.Raw_Material_Id=2 or rmp.Raw_Material_Id =3 or rmp.Raw_Material_Id =4) then ( rmp.sum - (i.sum*9 ))\n" +
            " end as 'count'  FROM \n" +
            "(((select Product_Id, Raw_Material_Id, sum(Quantity) as sum from Raw_Material_Purchase group by 1, 2) rmp\n" +
            "Inner join (select Product_Id, sum(Quantity) as sum from Inventory group by 1) i\n" +
            " ON rmp.Product_Id = i.Product_Id) Left join Product p on rmp.Product_Id = p.Product_Id)\n" +
            " Left join Raw_Material rm on rm.Raw_Material_Id = rmp.Raw_Material_Id\n" +
            " WHERE rmp.Product_Id=:productId group by 1;";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<MetricsResponse> getMetrics() throws SQLException {
        List<MetricsResponse> metricsResponseList = new ArrayList<>();
        try {
            metricsResponseList = this.namedParameterJdbcTemplate.query(GET_AVAILABLE_STOCK_AND_PENDING_ORDERS, new MapSqlParameterSource(), new MetricsResponseRowMapper());

            for (MetricsResponse metricsResponse: metricsResponseList) {
                MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
                mapSqlParameterSource.addValue("productId", metricsResponse.getProductId());
                List<RawMaterialDictionary> availableRawMaterials = this.namedParameterJdbcTemplate.query(GET_AVAILABLE_RAW_MATERIAL_LIST_BY_PRODUCT_ID, mapSqlParameterSource, new RawMaterialDictionaryRowMapper());
                metricsResponse.setAvailableRawMaterial(availableRawMaterials);
            }
            return metricsResponseList;
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }

}
