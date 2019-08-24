package com.github.commoble.rocksremastered.rocks;

public class RockLayer
{
	public RockType rockType;
	public double layerWeight;
	public double localVariationCode;
	
	public RockLayer(RockType rockType, double layerWeight, double localVariationCode)
	{
		this.rockType = rockType;
		this.layerWeight = layerWeight;
		this.localVariationCode = localVariationCode;
	}
}
