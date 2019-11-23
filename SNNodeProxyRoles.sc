SNNodeProxyRoles {
	*initClass {
		Class.initClassTree(AbstractPlayControl);

		// seti role
		AbstractPlayControl.proxyControlClasses.put(\seti, StreamControl);
		AbstractPlayControl.buildMethods.put(\seti,
			#{ |pattern, proxy, channelOffset = 0, index|
				var args = proxy.controlNames.collect(_.name);
				Pchain(
					(type: \set, id: { proxy.group.nodeID }, args: args),
					Pbindf(
						pattern,
						\play, Pfunc { |e| args.do { |n|
							e[n] !? {
								proxy.seti(n, e.channelOffset, e[n])
							}
						}}
					)
				).buildForProxy(proxy, channelOffset, index)
			}
		);

		AbstractPlayControl.proxyControlClasses.put(\setp, StreamControl);
		AbstractPlayControl.buildMethods.put(\setp,
			#{ |pattern, proxy, channelOffset = 0, index|
				var subMsg, id, synthDef, args;
				args = pattern.patternpairs.asEvent.keys.asArray;
				if (pattern.class == Pbind) {
					// OSCFunc({ |msg|
					// 	var subMsg = msg[(msg.size - (3 * 5))..msg.size-1];
					// 	id = subMsg[e.channelOffset * 3].postln;
					// 	// synthDef = subMsg[e.channelOffset * 3 + 2].postln;
					// }, '/g_queryTree.reply').oneShot;
					// Pchain(
						Pbindf(
							pattern,
							\type, \set,
							\args, args,
							\play, Pfunc { |e|
								e.postln;
								s.sendMsg('/g_queryTree', proxy.group.nodeID);
								OSCFunc({ |msg|
									var subMsg = msg[(msg.size - (3 * 5))..msg.size-1];
									msg.postln;
									id = subMsg[e.channelOffset * 3];
									// synthDef = subMsg[e.channelOffset * 3 + 2].postln;
								}, '/g_queryTree.reply').oneShot;
								args.do { |n|
									s.sendMsg('/n_set', id.postln, n, e[n])
								}
							},
							\id, id,
						).trace.buildForProxy(proxy, channelOffset, index)
					// Pfunc { |e|
					// e.postln;
					// s.sendMsg('/g_queryTree', proxy.group.nodeID);
					// OSCFunc({ |msg|
					// var subMsg = msg[(msg.size - (3 * 5))..msg.size-1];
					// msg.postln;
					// id = subMsg[e.channelOffset * 3];
					// synthDef = subMsg[e.channelOffset * 3 + 2].postln;
			// }, '/g_queryTree.reply').oneShot;
					// nil
				// }
				// ).buildForProxy(proxy, channelOffset, index)
				}
			}
		)
	}
}
