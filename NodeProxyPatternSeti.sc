NodeProxyPatternSeti {
	*initClass {
		Class.initClassTree(AbstractPlayControl);
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
		)
	}
}
