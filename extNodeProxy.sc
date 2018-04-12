+NodeProxy {
	seti { arg ... args; // pairs of keys or indices and value
		var msg = Array.new(args.size div: 3 * 2);
		"msg: %, msg size: %\n".postf(msg, msg.size);
		// "controlNames: %, controlKeys: %\n".postf(this.controlNames, this.controlKeys);
		// "nodeMap before: %\n".postf(nodeMap);
		// nodeMap.set(*args);
		// "nodeMap after: %\n".postf(nodeMap);
		forBy(0, args.size-1, 3, { |i|
			var key = args[i], offset = args[i+1], value = args[i+2];
			var hasControlKey = this.controlKeys.includes(key);
			var controlKeysValues, controlSize, controlIndex, controlValues;
			if (hasControlKey) {
				controlKeysValues = this.controlKeysValues;
				controlIndex = controlKeysValues.indexOf(key);
				controlValues = controlKeysValues[controlIndex + 1];
				controlSize = controlValues.size;
				if (offset < controlSize) {
					controlValues[offset] = value;
					nodeMap.set(*[key, controlValues]);
					msg.add(key).add(controlValues);
				}
			};
		});
		"nodeMap after: %\n".postf(nodeMap);
		if(this.isPlaying) {
			server.sendBundle(server.latency, [15, group.nodeID] ++ msg.asOSCArgArray);
		}
	}
}