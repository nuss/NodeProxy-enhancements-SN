TestNodeProxySeti : UnitTest {
	var proxy, server;
	setUp {
		server = Server.default;
		proxy = NodeProxy.audio(server, 5);
		proxy.source = { SinOsc.ar(\freq.kr(200!5), \phase.kr(0!5)) };
	}

	tearDown {
		proxy.clear;
	}

	test_seti {
		var keysValues;
		proxy.seti(\freq, 2, 212, \phase, 1, 0.5pi);
		keysValues = proxy.getKeysValues;
		this.assertEquals(keysValues[0][1][2], 212, "arg 'freq' should have been set to 212 for the third channel in NodeProxy 'proxy'.");
		this.assertEquals(keysValues[1][1][1], 0.5pi, "arg 'phase' should have been set to 0.5pi for the second channel in NodeProxy 'proxy'.");
	}

	test_seti_multi {
		var keysValues;
		proxy.seti(\freq, [1, 3], [212, 328]);
		keysValues = proxy.getKeysValues;
		this.assertEquals(keysValues[0][1], [200, 212, 200, 328, 200], "the 'freq' arg array should have been set to [200, 212, 212, 328, 200] in NodeProxy 'proxy'.");
	}

	test_seti_wrapAt {
		var keysValues;
		proxy.seti(\freq, [1, 23, 57], [345, 145]);
		keysValues = proxy.getKeysValues;
		this.assertEquals(keysValues[0][1], [200, 345, 345, 145, 200], "the 'freq' arg array should have been set to [200, 345, 345, 145, 200] in NodeProxy 'proxy'.");
	}
}
