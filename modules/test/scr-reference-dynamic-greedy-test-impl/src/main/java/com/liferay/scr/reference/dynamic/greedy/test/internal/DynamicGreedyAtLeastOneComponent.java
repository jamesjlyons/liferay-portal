/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.scr.reference.dynamic.greedy.test.internal;

import com.liferay.scr.reference.dynamic.greedy.test.DynamicGreedyComponent;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Preston Crary
 */
@Component(
	enabled = false, immediate = true,
	property = "reference.cardinality=at_least_one",
	service = DynamicGreedyComponent.class
)
public class DynamicGreedyAtLeastOneComponent
	implements DynamicGreedyComponent {

	@Override
	public List<String> getBindingCalls() {
		return _bindingCalls;
	}

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(reference.cardinality=at_least_one)"
	)
	protected void bindAtLeastOneDependency(Object object) {
		_bindingCalls.add("bindAtLeastOneDependency-" + object);
	}

	protected void unbindAtLeastOneDependency(Object object) {
		_bindingCalls.add("unbindAtLeastOneDependency-" + object);
	}

	private final List<String> _bindingCalls = new ArrayList<>();

}