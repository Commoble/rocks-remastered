package com.github.commoble.rocksremastered.rocks;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum EnumRockClassification
{
	PLUTONIC("plutonic"),
	VOLCANIC("volcanic"),
	METAMORPHIC("metamorphic"),
	SEDIMENTARY("sedimentary");
	
	public final String NAME;
	
	private EnumRockClassification(String name)
	{
		this.NAME = name;
	}
	
	private static final Map<String, EnumRockClassification> NAME_MAP = Arrays.stream(EnumRockClassification.values())
			.collect(Collectors.toMap(
					rock -> {return rock.NAME;},
					rock -> {return rock;}
					));
	
	public static EnumRockClassification getClassification(String str)
	{
		return NAME_MAP.get(str);
	}
}
