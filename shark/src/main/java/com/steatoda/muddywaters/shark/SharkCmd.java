package com.steatoda.muddywaters.shark;

import java.io.IOException;

import org.rapidoid.setup.On;
import org.rapidoid.setup.Setup;

public class SharkCmd {

	public static final String Path = "/eat";
	
	public static void main(String[] args) throws IOException {

		System.out.println("Yo!");

		Setup setup = On.setup();
		
		setup.post(Path).managed(false).serve(new Handler());

		System.out.println("http://" + setup.address() + ":" + setup.port() + Path);

	}

}
