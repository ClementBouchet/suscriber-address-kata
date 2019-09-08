package fr.lacombe.Model.Request;

import fr.lacombe.Model.ContractList;

public class ContractListRequest extends Request{
    private ContractList contractList;

    public ContractListRequest(ContractList contractList) {
        this.contractList = contractList;
    }

    public ContractList getContractList() {
        return contractList;
    }

    public void setContractList(ContractList contractList) {
        this.contractList = contractList;
    }
}
