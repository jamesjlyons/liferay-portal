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

package com.liferay.layout.admin.web.internal.display.context;

import com.liferay.layout.admin.web.internal.util.LayoutPageTemplatePortletUtil;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.LayoutTypeController;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.util.LayoutTypeControllerTracker;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class SelectLayoutPageTemplateEntryDisplayContext {

	public SelectLayoutPageTemplateEntryDisplayContext(
		HttpServletRequest request) {

		_request = request;

		_themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<LayoutPageTemplateEntry> getGlobalLayoutPageTemplateEntries() {
		OrderByComparator<LayoutPageTemplateEntry> orderByComparator =
			LayoutPageTemplatePortletUtil.
				getLayoutPageTemplateEntryOrderByComparator("name", "asc");

		return LayoutPageTemplateEntryServiceUtil.getLayoutPageTemplateEntries(
			_themeDisplay.getCompanyGroupId(),
			LayoutPageTemplateEntryTypeConstants.TYPE_WIDGET_PAGE,
			WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, orderByComparator);
	}

	public int getGlobalLayoutPageTemplateEntriesCount() {
		return LayoutPageTemplateEntryServiceUtil.
			getLayoutPageTemplateEntriesCount(
				_themeDisplay.getCompanyGroupId(),
				LayoutPageTemplateEntryTypeConstants.TYPE_WIDGET_PAGE,
				WorkflowConstants.STATUS_APPROVED);
	}

	public long getLayoutPageTemplateCollectionId() {
		if (_layoutPageTemplateCollectionId != null) {
			return _layoutPageTemplateCollectionId;
		}

		_layoutPageTemplateCollectionId = ParamUtil.getLong(
			_request, "layoutPageTemplateCollectionId");

		return _layoutPageTemplateCollectionId;
	}

	public List<LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		int start, int end) {

		return LayoutPageTemplateEntryServiceUtil.getLayoutPageTemplateEntries(
			_themeDisplay.getScopeGroupId(),
			getLayoutPageTemplateCollectionId(),
			WorkflowConstants.STATUS_APPROVED, start, end);
	}

	public int getLayoutPageTemplateEntriesCount() {
		return LayoutPageTemplateEntryServiceUtil.
			getLayoutPageTemplateEntriesCount(
				_themeDisplay.getScopeGroupId(),
				getLayoutPageTemplateCollectionId());
	}

	public List<String> getPrimaryTypes() {
		if (_primaryTypes != null) {
			return _primaryTypes;
		}

		_primaryTypes = ListUtil.filter(
			ListUtil.fromArray(LayoutTypeControllerTracker.getTypes()),
			type -> {
				LayoutTypeController layoutTypeController =
					LayoutTypeControllerTracker.getLayoutTypeController(type);

				return layoutTypeController.isInstanceable() &&
					layoutTypeController.isPrimaryType();
			});

		return _primaryTypes;
	}

	public int getPrimaryTypesCount() {
		List<String> types = getPrimaryTypes();

		return types.size();
	}

	public String getSelectedTab() {
		if (_selectedTab != null) {
			return _selectedTab;
		}

		_selectedTab = ParamUtil.getString(
			_request, "selectedTab", "basic-pages");

		return _selectedTab;
	}

	public List<String> getTypes() {
		if (_types != null) {
			return _types;
		}

		_types = ListUtil.filter(
			ListUtil.fromArray(LayoutTypeControllerTracker.getTypes()),
			type -> {
				LayoutTypeController layoutTypeController =
					LayoutTypeControllerTracker.getLayoutTypeController(type);

				return layoutTypeController.isInstanceable() &&
					!layoutTypeController.isPrimaryType();
			});

		return _types;
	}

	public int getTypesCount() {
		List<String> types = getTypes();

		return types.size();
	}

	public boolean isBasicPages() {
		if (getLayoutPageTemplateCollectionId() != 0) {
			return false;
		}

		if (!Objects.equals(getSelectedTab(), "basic-pages")) {
			return false;
		}

		return true;
	}

	public boolean isContentPages() {
		if (getLayoutPageTemplateCollectionId() != 0) {
			return true;
		}

		return false;
	}

	public boolean isGlobalTemplates() {
		if (getLayoutPageTemplateCollectionId() != 0) {
			return false;
		}

		if (!Objects.equals(getSelectedTab(), "global-templates")) {
			return false;
		}

		return true;
	}

	private Long _layoutPageTemplateCollectionId;
	private List<String> _primaryTypes;
	private final HttpServletRequest _request;
	private String _selectedTab;
	private final ThemeDisplay _themeDisplay;
	private List<String> _types;

}