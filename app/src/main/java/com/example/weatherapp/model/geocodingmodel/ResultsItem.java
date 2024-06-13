package com.example.weatherapp.model.geocodingmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResultsItem{

	@SerializedName("formatted_address")
	private String formattedAddress;

	@SerializedName("types")
	private List<String> types;

	@SerializedName("geometry")
	private Geometry geometry;

	@SerializedName("address_components")
	private List<AddressComponentsItem> addressComponents;

	@SerializedName("plus_code")
	private PlusCode plusCode;

	@SerializedName("place_id")
	private String placeId;

	public void setFormattedAddress(String formattedAddress){
		this.formattedAddress = formattedAddress;
	}

	public String getFormattedAddress(){
		return formattedAddress;
	}

	public void setTypes(List<String> types){
		this.types = types;
	}

	public List<String> getTypes(){
		return types;
	}

	public void setGeometry(Geometry geometry){
		this.geometry = geometry;
	}

	public Geometry getGeometry(){
		return geometry;
	}

	public void setAddressComponents(List<AddressComponentsItem> addressComponents){
		this.addressComponents = addressComponents;
	}

	public List<AddressComponentsItem> getAddressComponents(){
		return addressComponents;
	}

	public void setPlusCode(PlusCode plusCode){
		this.plusCode = plusCode;
	}

	public PlusCode getPlusCode(){
		return plusCode;
	}

	public void setPlaceId(String placeId){
		this.placeId = placeId;
	}

	public String getPlaceId(){
		return placeId;
	}
}