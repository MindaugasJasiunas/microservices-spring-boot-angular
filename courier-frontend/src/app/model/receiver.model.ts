import { Address } from "./address.model";

export class Receiver {
  constructor(
    public firstName: string,
    public lastName: string,
    public phoneNumber: string,
    public email: string,
    public address: Address,
    public company?: string
    ){}
  }
