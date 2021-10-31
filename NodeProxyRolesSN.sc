NodeProxyRolesSN {
	*initClass {
		Class.initClassTree(AbstractPlayControl);

		// seti role: set controls for a specific channel in a multi channel NodeProxy
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

		// manipulate nodes (grains) generated through a pattern (a Pbind) embedded in a NodeProxy
		// pattern in NodeProxy will not be traceable!!
		AbstractPlayControl.proxyControlClasses.put(\setp, StreamControl);
		AbstractPlayControl.buildMethods.put(\setp,
			#{ |pattern, proxy, channelOffset = 0, index|
				var subMsg, id, synthDef, args;
				args = pattern.patternpairs.asEvent.keys.asArray;
				if (pattern.class == Pbind) {
					Pbindf(
						pattern,
						\type, \set,
						\args, args,
						\play, Pfunc { |e|
							s.sendMsg('/g_queryTree', proxy.group.nodeID);
							OSCFunc({ |msg|
								// slots 0-3 contain the command name and info on the group itself
								// we only need to know the ids of the synths within the group
								var subMsg = msg[4..];
								if (e.channelOffset.notNil) {
									id = subMsg[e.channelOffset * 3];
								} {
									id = 0;
								};
								args.do { |n|
									s.sendMsg('/n_set', id, n, e[n])
								}
							}, '/g_queryTree.reply').oneShot;
						},
					).buildForProxy(proxy, channelOffset, index)
				}
			}
		)
	}
}
