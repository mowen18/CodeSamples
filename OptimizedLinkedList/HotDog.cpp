//-----------------------------------------------------------------------------
// Copyright 2022, Ed Keenan, all rights reserved.
//----------------------------------------------------------------------------- 

#include "HotDog.h"

// -----------------------------------------------
// AZUL_REPLACE_ME_STUB
// this is place holder for compiling purposes
// Delete each AZUL_REPLACE_ME_STUB line
// Replace with your own code
// -----------------------------------------------

// Methods

HotDog::HotDog()
{
	this->next = nullptr;
	this->prev = nullptr;
	this->condiments = static_cast<unsigned int> (Condiments::Plain);
}
//copy
HotDog::HotDog(const HotDog& dog)
{
	this->next = nullptr;
	this->prev = nullptr;
	//this->next = dog.next;
	//this->prev = dog.prev;
	this->condiments = dog.condiments;
}
HotDog::HotDog(const HotDog* _dog)
{
	if (_dog == nullptr)
		return;
	if (_dog->next != nullptr) {
		this->next = new HotDog(_dog->next);
	}
	else {
		this->next = nullptr;
	}
	if (_dog->prev != nullptr) {
		this->prev = _dog->prev;
	}
	else {
		this->prev = nullptr;
	}

	this->condiments = _dog->condiments;
}
//overload
HotDog& HotDog::operator = (const HotDog& dog)
{
	this->next = nullptr;
	this->prev = nullptr;
	//this->next = dog.next;
	//this->prev = dog.prev;
	this->condiments = dog.condiments;
	return *this;

}
//destructor
HotDog::~HotDog() {
	//nothin
}

void HotDog::Minus(const Condiments condiment)
{
	// Only use bit-wise operations
	unsigned int c = this->condiments ^ static_cast<unsigned int>(condiment);
	if(c < this->condiments){
		this->condiments = this->condiments ^ static_cast<unsigned int>(condiment);
	}
	
	
}

void HotDog::Add(const Condiments condiment)
{
	// Only use bit-wise operations
	if (this == nullptr)
		return;
	this->condiments |= static_cast<unsigned int>(condiment);
}

unsigned int HotDog::GetCondiments() const
{
	if(this !=NULL)
		return this->condiments;
	return 0;
}

HotDog * const HotDog::GetNext() const
{
	if (this == nullptr) {
		//printf("nullptr returned from GetNext");
		return nullptr;
	}
	
	return next;
}

HotDog * const HotDog::GetPrev() const
{
	return this->prev;
}

void HotDog::SetNext(HotDog *pDog)
	
{
	if (this == NULL)
		return;
	this->next = pDog;
}

void HotDog::SetPrev(HotDog *pDog)
{
	if (this == NULL)
		return;
	this->prev = pDog;
}


// ---  End of File ---------------

