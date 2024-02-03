//-----------------------------------------------------------------------------
// Copyright 2022, Ed Keenan, all rights reserved.
//----------------------------------------------------------------------------- 

#include "Order.h"

// -----------------------------------------------
// AZUL_REPLACE_ME_STUB
// this is place holder for compiling purposes
// Delete each AZUL_REPLACE_ME_STUB line
// Replace with your own code
// -----------------------------------------------

// Methods

Order::Order()
{
	this->name = Name::Unknown;
	this->head = nullptr;
	this->next = nullptr;
	this->prev = nullptr;
}

Order::Order(Name _name)
{
	
	this->head = nullptr;
	this->name = _name;
	this->next = nullptr;
	this->prev = nullptr;
	
}
Order& Order::operator=(const Order& _order)
{
	
	this->head = new HotDog(_order.head);
	this->next = nullptr;
	this->name = _order.name;
	return *this;


}
Order::Order(const Order* _in)
{
	
	if (_in == nullptr)
		return;
	this->head = nullptr;
	this->next = nullptr;
	this->prev = nullptr;
	this->name = _in->name;
	
}

Order::Order(const Order& _in)
{ 
	if (_in.head == nullptr)
		return;
	this->head = new HotDog(_in.head);
	this->name = _in.name;
	if(_in.next != nullptr)
		this->next = new Order(_in.next);
	else {
		this->next = nullptr;
	}
	if(_in.prev != nullptr)
		this->prev = new Order(_in.prev);
	else {
		this->prev = nullptr;
	}
	
}
Order::~Order()
{
	HotDog* index = this->head;
	while (index->GetNext() != nullptr)
	{
		HotDog* tmp = index->GetNext();
		delete index;
		index = tmp;
	}
	head = nullptr;
	delete index;
}


void Order::Add(HotDog *p)
{
	
	
	if (this->head == nullptr)
	{
		p->SetPrev(nullptr);
		this->head = p;
		return;
	}
	HotDog* last = this->head;
	while (last->GetNext() != nullptr)
		last = last->GetNext();
	last->SetNext(p);
	p->SetPrev(last);
	return;
}

void Order::Remove(HotDog *p)
{
	HotDog* tmp = this->head;
	
	if (this->head == p) {
		this->head = p->GetNext();
		this->head->SetPrev(nullptr);
		delete tmp;
		return;
	}
	while (tmp->GetNext() != nullptr)
	{
		tmp = tmp->GetNext();
		if (tmp == p)
		{
			HotDog* tmp2 = tmp->GetNext();
			tmp->GetPrev()->SetNext(tmp2);
			tmp2->SetPrev(tmp->GetPrev());
			delete tmp;
			return;
		}
			
	}
	

}

Order * const Order::GetNext() const
{
	if (this == nullptr)
		return nullptr;
	return this->next;
	//return nullptr;
}

Order * const Order::GetPrev() const
{
	//if (this->prev != nullptr)
	return this->prev;
	//return nullptr;
}

HotDog * const Order::GetHead() const
{
	return this->head;
}

void Order::SetNext(Order *p)
{
	if (this == nullptr)
		return;
	this->next = p;
}

void Order::SetPrev(Order *p)
{
	if (this == nullptr)
		return;
	this->prev = p;
}

Name Order::GetName() const
{
	//if (this == nullptr)
		//return;
	return this->name;
}

void Order::SetName(Name _name)
{
	this->name = _name;
}


//---  End of File ---
