package com.github.commoble.rocksremastered.rocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.github.commoble.rocksremastered.RocksRemastered;
import com.google.gson.stream.JsonReader;

public class RockJsonReader
{
	public static List<RockType> getRocksFromJson()
	{
		InputStream rockstream = RocksRemastered.class.getClassLoader().getResourceAsStream("data/rocksremastered/rocktypes.json");
		JsonReader reader = null;
		try
		{
			reader = new JsonReader(new InputStreamReader(rockstream, "UTF-8"));
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			return readRockJson(reader);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static List<RockType> readRockJson(JsonReader reader) throws IOException
	{
		List<RockType> rocks = new ArrayList<RockType>();
		
		reader.beginObject();
		while(reader.hasNext())
		{
			String className = reader.nextName();
			EnumRockClassification rockClass = EnumRockClassification.getClassification(className);
			if (rockClass != null)
			{
				reader.beginArray();
				while (reader.hasNext())
				{
					String rockName = reader.nextString();
					rocks.add(new RockType(rockName, rockClass));
				}
				reader.endArray();
			}
		}
		reader.endObject();
		
		return rocks;
	}
}
