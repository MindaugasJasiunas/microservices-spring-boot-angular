import { Receiver } from "./receiver.model"
import { Sender } from "./sender.model"

export class Package {
  constructor(
    public publicId: string | null,
    public trackingNumber: string,
    public packageContentsDescription: string,
    public packageStatus: string,
    public packageWeight: number,

    public fragile: boolean,
    public isReturn: boolean,
    public numberOfPackages: number,

    public sender: Sender,
    public receiver: Receiver,

    public senderId?: string,
    public receiverId?: string,
    public lastModifiedDate?: string,
    public createdDate?: string,
  ) {}
}
