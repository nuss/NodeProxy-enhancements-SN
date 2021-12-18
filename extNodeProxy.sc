+NodeProxy {

	seti { arg ... args; // pairs of key, index/indices (array) and value/values (array)
		var msg = Array.new(args.size div: 3 * 2);
		var key, offset, value, controlKeys, hasControlKey;
		var controlKeysValues, controlSize, controlIndex, controlValues;

		controlKeys = this.controlKeys;
		controlKeysValues = this.controlKeysValues;

		forBy(0, args.size-1, 3, { |i|
			key = args[i];
			offset = args[i+1].asArray;
			value = args[i+2].asArray;
			hasControlKey = controlKeys.includes(key);

			if(hasControlKey) {
				controlIndex = controlKeysValues.indexOf(key);
				controlValues = controlKeysValues[controlIndex + 1];
				controlSize = controlValues.size;
				offset.do { |o, j|
					controlValues[o % controlSize] = value.wrapAt(j);
				};
				nodeMap.set(*[key, controlValues]);
				msg.add(key).add(controlValues);
			};
		});

		if(this.isPlaying) {
			server.sendBundle(server.latency, [15, group.nodeID] ++ msg.asOSCArgArray);
		}
	}

	*test_seti {
		TestNodeProxySeti.runTest("TestNodeProxySeti:test_seti");
		TestNodeProxySeti.runTest("TestNodeProxySeti:test_seti_multi");
		TestNodeProxySeti.runTest("TestNodeProxySeti:test_seti_wrapAt");
		TestNodeProxySeti.runTest("TestNodeProxySeti:test_seti_nodeMap");
	}
}
