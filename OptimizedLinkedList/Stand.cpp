//-----------------------------------------------------------------------------
// Copyright 2022, Ed Keenan, all rights reserved.
//----------------------------------------------------------------------------- 

#include "Stand.h"

// -----------------------------------------------
// AZUL_REPLACE_ME_STUB
// this is place holder for compiling purposes
// Delete each AZUL_REPLACE_ME_STUB line
// Replace with your own code
// -----------------------------------------------

// Methods

Stand::Stand()
{
	this->head = nullptr;
	this->head->SetNext(nullptr);
	this->head->SetPrev(nullptr);
	this->current = 0;
	this->peak = 0;
}
Stand::Stand(const Stand& _stand) {
	this->head = _stand.head;
	this->head->SetNext(_stand.head->GetNext());
	this->head->SetPrev(_stand.head->GetPrev());
	this->current = _stand.current;
	this->peak = _stand.peak;
}

Stand::~Stand()
{
	Order* p = this->head->GetNext();
	while (p != nullptr) {
		Order* s = p;
		p = p->GetNext();
		// Trace::out("    ---> Cancel order:%s \n", s->orderToString(s->getName()));
		delete s;
	}
	delete head;
	//delete p;
	
}

int Stand::GetCurrOrders() const
{
	return this->current;
}

int Stand::GetPeakOrders() const
{
	return this->peak;
}

Order * const Stand::GetHead()
{
	return this->head;
}

void Stand::Remove(const Name name)
{
	Order* tmp = this->head;
	
	if (this->head->GetName() == name) {
		this->head = this->head->GetNext();
		this->head->SetPrev(nullptr);
		delete tmp;
		this->current--;
		return;
	}
	while (tmp->GetNext() != nullptr)
	{
		tmp = tmp->GetNext();
		if (tmp->GetName() == name)
		{
			Order* tmp2 = tmp->GetNext();
			tmp->GetPrev()->SetNext(tmp2);
			tmp2->SetPrev(tmp->GetPrev());
			this->current--;
			delete tmp;
			return;
		}

	}
}

void Stand::Add(Order * const pOrder)
{
	//Order* ord = pOrder;
	//Order* tmp = this->head;
	if (head == nullptr) {
		this->head = pOrder;
		this->head->SetName(pOrder->GetName());
		this->head->SetPrev(pOrder->GetPrev());
		this->head->SetNext(pOrder->GetNext());
		this->current++;
		
	}
	
	else {
		Order* last = this->head;
		while (last->GetNext() != nullptr)
			last = last->GetNext();
		last->SetNext(pOrder);
		pOrder->SetPrev(last);
		this->current++;
		

	}
	if (this->current > this->peak) {
		this->peak = this->current;
	}
}

//---  End of File ---
