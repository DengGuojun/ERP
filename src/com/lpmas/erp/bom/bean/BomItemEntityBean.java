package com.lpmas.erp.bom.bean;

import com.lpmas.pdm.business.WareInfoMediator;

public class BomItemEntityBean {
	private BomItemBean bomItemBean;
	WareInfoMediator wareInfoMediator = new WareInfoMediator();

	public BomItemEntityBean(BomItemBean bomItemBean) {
		super();
		this.bomItemBean = bomItemBean;
	}

	public BomItemBean getBomItemBean() {
		return bomItemBean;
	}

	public void setBomItemBean(BomItemBean bomItemBean) {
		this.bomItemBean = bomItemBean;
	}

	public String getWareName() {
		return wareInfoMediator.getWareNameByKey(bomItemBean.getWareType(), bomItemBean.getWareId());
	}

	public String getWareNumber() {
		return wareInfoMediator.getWareNumberByKey(bomItemBean.getWareType(), bomItemBean.getWareId());
	}

	public String getSpecification() {
		return wareInfoMediator.getWareSpecificationByKey(bomItemBean.getWareType(), bomItemBean.getWareId());
	}

	public int getBomItemId() {
		return bomItemBean.getBomId();
	}

	public int getBomId() {
		return bomItemBean.getBomId();
	}

	public int getUsageType() {
		return bomItemBean.getUsageType();
	}

	public int getWareType() {
		return bomItemBean.getWareType();
	}

	public int getWareId() {
		return bomItemBean.getWareId();
	}

	public String getUnit() {
		return bomItemBean.getUnit();
	}

	public String getUnitName() {
		return wareInfoMediator.getWareUnitNameByKey(bomItemBean.getWareType(), bomItemBean.getWareId());
	}

	public double getQuantity() {
		return bomItemBean.getQuantity();
	}

	public String getItemValue() {
		return "itemId=" + String.valueOf(bomItemBean.getBomItemId()) + ",wareId=" + String.valueOf(bomItemBean.getWareId()) + ",wareType="
				+ String.valueOf(bomItemBean.getWareType()) + ",unit=" + bomItemBean.getUnit() + ",quatity="
				+ String.valueOf(bomItemBean.getQuantity()) + ",usageType=" + String.valueOf(bomItemBean.getUsageType());
	}

}
