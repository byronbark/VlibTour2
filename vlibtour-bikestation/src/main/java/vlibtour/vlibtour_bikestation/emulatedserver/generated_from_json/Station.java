
package vlibtour.vlibtour_bikestation.emulatedserver.generated_from_json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "number",
    "name",
    "address",
    "position",
    "banking",
    "bonus",
    "status",
    "contract_name",
    "bike_stands",
    "available_bike_stands",
    "available_bikes",
    "last_update"
})
public class Station {

    @JsonProperty("number")
    public long number;
    @JsonProperty("name")
    public String name;
    @JsonProperty("address")
    public String address;
    @JsonProperty("position")
    public Position position;
    @JsonProperty("banking")
    public boolean banking;
    @JsonProperty("bonus")
    public boolean bonus;
    @JsonProperty("status")
    public String status;
    @JsonProperty("contract_name")
    public String contractName;
    @JsonProperty("bike_stands")
    public long bikeStands;
    @JsonProperty("available_bike_stands")
    public long availableBikeStands;
    @JsonProperty("available_bikes")
    public long availableBikes;
    @JsonProperty("last_update")
    public long lastUpdate;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Station() {
    }

    /**
     * 
     * @param position
     * @param bikeStands
     * @param status
     * @param address
     * @param lastUpdate
     * @param name
     * @param availableBikeStands
     * @param banking
     * @param bonus
     * @param number
     * @param contractName
     * @param availableBikes
     */
    public Station(long number, String name, String address, Position position, boolean banking, boolean bonus, String status, String contractName, long bikeStands, long availableBikeStands, long availableBikes, long lastUpdate) {
        super();
        this.number = number;
        this.name = name;
        this.address = address;
        this.position = position;
        this.banking = banking;
        this.bonus = bonus;
        this.status = status;
        this.contractName = contractName;
        this.bikeStands = bikeStands;
        this.availableBikeStands = availableBikeStands;
        this.availableBikes = availableBikes;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("number", number).append("name", name).append("address", address).append("position", position).append("banking", banking).append("bonus", bonus).append("status", status).append("contractName", contractName).append("bikeStands", bikeStands).append("availableBikeStands", availableBikeStands).append("availableBikes", availableBikes).append("lastUpdate", lastUpdate).toString();
    }

    /**
     * Compare the number of the station with a number given.
     * 
     * @param number
     *            the station number to compare.
     * @return boolean value to indicate if the number correspond to the station's number.
     */
    public Boolean sameNumber(final long number){
        return this.number == number;
    }

    public Boolean nearGPSPosition(final double latitude, final double longitude, final double distance){
        return this.position.nearTo(latitude, longitude, distance);
    }

    public double distanceFrom(final double latitude, final double longitude){
        return this.position.distanceFrom(latitude, longitude);
    }

}
