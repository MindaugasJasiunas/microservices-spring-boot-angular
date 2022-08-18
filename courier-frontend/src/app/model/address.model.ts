export class Address{
  constructor(
    public address1: string,
    public apartmentNumber: string,
    public city: string,
    public houseNumber: string,
    public postalCode: string,
    public state: string,
    public address2?: string,
    public address3?: string,
  ){}
}
