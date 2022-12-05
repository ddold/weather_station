package org.example.controllers;

import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.example.entities.WeatherData;
import org.example.exceptions.QueryException;
import org.example.services.WeatherDataService;

@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WeatherDataController {

    @Inject
    WeatherDataService service;

    @POST
    public Response postWeatherData(@NotNull @Valid WeatherData weatherData) {
        WeatherData createdWeatherData = service.saveWeatherData(weatherData);
        return Response
                .status(Response.Status.CREATED)
                .entity(createdWeatherData)
                .build();
    }

    @GET
    public Response getWeatherData(
            @QueryParam("sensor") List<String> sensorIds,
            @QueryParam("metric") List<String> metric,
            @QueryParam("statistic") String statistic,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd
    ) throws QueryException {
        Map<String, Object> response = service.getWeatherData(sensorIds, metric, statistic, dateStart, dateEnd);
        return Response
                .ok(response)
                .build();
    }

}
