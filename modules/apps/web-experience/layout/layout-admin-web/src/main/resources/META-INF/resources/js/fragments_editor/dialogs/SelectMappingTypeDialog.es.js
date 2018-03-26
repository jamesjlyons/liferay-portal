import Component from 'metal-component';
import {Config} from 'metal-state';
import Soy from 'metal-soy';

import 'frontend-js-web/liferay/compat/modal/Modal.es';
import templates from './SelectMappingTypeDialog.soy';

/**
 * SelectMappingTypeDialog
 */

class SelectMappingTypeDialog extends Component {

	/**
	 * @inheritDoc
	 * @review
	 */

	rendered() {
		if (this.visible && !this._mappingTypes) {
			this._loadMappingTypes();
		}
	}

	/**
	 * Emits a mappingTypeSelected event with the corresponding labels
	 * @review
	 */

	_emitSelectedMappingLabels() {
		const label = {
			subtype: '',
			type: ''
		};
		const subtype = this._mappingSubtypes.find(
			subtype => subtype.id === this._selectedMappingSubtypeId
		);
		const type = this._mappingTypes.find(
			type => type.id === this._selectedMappingTypeId
		);

		if (subtype) {
			label.subtype = subtype.label;
		}

		if (type) {
			label.type = type.label;
		}

		this.emit('mappingTypeSelected', {label});
	}

	/**
	 * Close asset type selection dialog
	 * @private
	 * @review
	 */

	_handleCloseButtonClick() {
		this.visible = false;
	}

	/**
	 * Callback executed when a mapping subtype has been selected.
	 * @private
	 * @review
	 */

	_handleMappingSubtypeSelectChange() {
		const selectInput = this.refs.modal.refs.selectMappingSubtype;

		const mappingSubtype = this._mappingSubtypes.find(
			mappingSubtype => mappingSubtype.id === selectInput.value
		);

		this._selectedMappingSubtypeId = mappingSubtype.id;
	}

	/**
	 * Callback executed when a mapping type has been selected.
	 * It loads a mapping subtype list if necessary.
	 * @private
	 * @review
	 */

	_handleMappingTypeSelectChange() {
		const selectInput = this.refs.modal.refs.selectMappingType;

		const mappingType = this._mappingTypes.find(
			mappingType => mappingType.id === selectInput.value
		);

		this._selectedMappingTypeId = mappingType.id;
		this._mappingSubtypes = [];

		if (mappingType.hasSubtypes) {
			this._loadMappingSubtypes();
		}
	}

	/**
	 * Sends selected mapping type and subtype to the server
	 * and closes this dialog.
	 * @private
	 * @review
	 */

	_handleSubmitButtonClick() {
		this._savingChanges = true;

		setTimeout(
			() => {
				this._emitSelectedMappingLabels();
				this.visible = false;
			},
			1000
		);
	}

	/**
	 * Change asset type selection dialog visibility.
	 * @private
	 * @review
	 */

	_handleVisibleChanged(change) {
		if (this.visible !== change.newVal) {
			this.visible = change.newVal;
		}

		if (!change.newVal) {
			this._mappingSubtypes = [];
			this._mappingTypes = [];
			this._savingChanges = false;
			this._selectedMappingTypeId = '';
			this._selectedMappingSubtypeId = '';
		}
	}

	/**
	 * Load a list of mapping subtypes.
	 * @private
	 * @review
	 */

	_loadMappingSubtypes() {
		this._mappingSubtypes = [];
	}

	/**
	 * Load a list of mapping types.
	 * @private
	 * @review
	 */

	_loadMappingTypes() {
		this._mappingTypes = []
	}
}

/**
 * State definition.
 * @review
 * @static
 * @type {!Object}
 */

SelectMappingTypeDialog.STATE = {

	/**
	 * Class primary key used for storing changes.
	 * @default undefined
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @review
	 * @type {!string}
	 */

	classPK: Config.string().required(),

	/**
	 * URL for obtaining the asset types for which asset display pages can be
	 * created.
	 * @default undefined
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @review
	 * @type {!string}
	 */

	getAssetClassTypesURL: Config.string().required(),

	/**
	 * URL for obtaining the asset types for which asset display pages can be
	 * created.
	 * @default undefined
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @review
	 * @type {!string}
	 */

	getAssetDisplayContributorsURL: Config.string().required(),

	/**
	 * Portlet namespace needed for prefixing form inputs
	 * @default undefined
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @review
	 * @type {!string}
	 */

	portletNamespace: Config.string().required(),

	/**
	 * Path of the available icons.
	 * @default undefined
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @review
	 * @type {!string}
	 */

	spritemap: Config.string().required(),

	/**
	 * URL for updating the asset type associated to a template.
	 * @default undefined
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @review
	 * @type {!string}
	 */

	updateLayoutPageTemplateEntryAssetTypeURL: Config.string().required(),

	/**
	 * Whether to show the mapping dialog or not
	 * @default undefined
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @review
	 * @type {!boolean}
	 */

	visible: Config.bool().required(),

	/**
	 * List of available mapping types
	 * @default null
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @private
	 * @review
	 * @type {Array<{
	 *   hasSubtypes: !boolean,
	 *   id: !string,
	 *   label: !string
	 * }>}
	 */

	_mappingTypes: Config
		.arrayOf(
			Config.shapeOf(
				{
					hasSubtypes: Config.bool().required(),
					id: Config.string().required(),
					label: Config.string().required()
				}
			)
		)
		.value(null),

	/**
	 * List of available mapping subtypes
	 * @default null
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @private
	 * @review
	 * @type {Array<{
	 *   id: !string,
	 *   label: !string
	 * }>}
	 */

	_mappingSubtypes: Config
		.arrayOf(
			Config.shapeOf(
				{
					id: Config.string().required(),
					label: Config.string().required()
				}
			)
		)
		.value([]),

	/**
	 * Flag indicating if changes are being sent to the server
	 * @default false
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @private
	 * @review
	 * @type {boolean}
	 */

	_savingChanges: Config
		.bool()
		.internal()
		.value(false),

	/**
	 * String with the selected mapping type id
	 * @default ''
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @private
	 * @review
	 * @type {string}
	 */

	_selectedMappingTypeId: Config
		.string()
		.internal()
		.value(''),

	/**
	 * String with the selected mapping subtype id
	 * @default ''
	 * @instance
	 * @memberOf SelectMappingTypeDialog
	 * @private
	 * @review
	 * @type {string}
	 */

	_selectedMappingSubtypeId: Config
		.string()
		.internal()
		.value('')

};

Soy.register(SelectMappingTypeDialog, templates);

export {SelectMappingTypeDialog};
export default SelectMappingTypeDialog;