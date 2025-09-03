package com.herts.shipment;

import com.herts.model.Offer;
import com.herts.model.Vendor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class DistanceMatrixService {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public double[] getCoordinates(String postcode) throws IOException {
        String url = "https://api.postcodes.io/postcodes/" + postcode.replaceAll("\\s+", "");
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            JSONObject obj = new JSONObject(json);
            if (!obj.getString("status").equals("200")) {
                throw new IOException("Invalid postcode: " + postcode);
            }
            JSONObject result = obj.getJSONObject("result");
            return new double[]{result.getDouble("latitude"), result.getDouble("longitude")};
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static double calculateDistanceKm(double[] coord1, double[] coord2) {
        double lat1 = coord1[0];
        double lon1 = coord1[1];
        double lat2 = coord2[0];
        double lon2 = coord2[1];

        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public LocalDate estimateDeliveryDate(String originPostcode, String destinationPostcode, Vendor vendor, Offer offer)  {
      double distance;

      try {
          double[] originCords = this.getCoordinates(originPostcode);
          double[] destCords = this.getCoordinates(destinationPostcode);

          distance = calculateDistanceKm(originCords, destCords);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
        int estimatedDays = Math.max(1, (int) Math.ceil(distance / vendor.getMindist()));

        offer.setEstimateDelivery(String.valueOf(LocalDate.now().plusDays(estimatedDays)));
        offer.setOfferCost(distance* vendor.getDistCost()+(estimatedDays*vendor.getMinCost()));
        return LocalDate.now().plusDays(estimatedDays);
    }
    }
