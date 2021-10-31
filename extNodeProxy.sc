+NodeProxy {
	seti { arg ... args; // pairs of key, index/indices (array) and value/values (array)
		var msg = Array.new(args.size div: 3 * 2);
		var key, offset, value, hasControlKey;
		var controlKeysValues, controlSize, controlIndex, controlValues;

		forBy(0, args.size-1, 3, { |i|
			key = args[i];
			offset = args[i+1];
			value = args[i+2];
			hasControlKey = this.controlKeys.includes(key);

			if (offset.isInteger) {
				offset = [offset]
			};

			if (value.isNil or:{ value.isNumber }) {
				value = [value]
			};

			if (hasControlKey) {
				controlKeysValues = this.controlKeysValues;
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
		if (this.isPlaying) {
			server.sendBundle(server.latency, [15, group.nodeID] ++ msg.asOSCArgArray);
		}
	}

	*test_seti {
		TestNodeProxySeti.runTest("TestNodeProxySeti:test_seti");
		TestNodeProxySeti.runTest("TestNodeProxySeti:test_seti_multi");
		TestNodeProxySeti.runTest("TestNodeProxySeti:test_seti_wrapAt");
	}
}
