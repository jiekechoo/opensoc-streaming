package com.opensoc.topology;

import org.apache.commons.configuration.ConfigurationException;

import backtype.storm.generated.InvalidTopologyException;

import com.opensoc.topology.runner.M0n0wallRunner;
import com.opensoc.topology.runner.TopologyRunner;

/**
 * Topology for processing M0n0wall messages
 *
 */
public class M0n0wall {

	public static void main(String[] args) throws ConfigurationException, Exception, InvalidTopologyException {

		TopologyRunner runner = new M0n0wallRunner();
		runner.initTopology(args, "m0n0wall");
	}

}
