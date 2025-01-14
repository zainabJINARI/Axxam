export interface Announce {
  id: string;
  title: string;
  description: string;
  address: string;
  priceForNight: number;
  photos: string[];
  dateCreation: Date;
  //   category: Category;
  hostId: number;
  //   host?: HostModel;
  //   services?: ServiceA[];
}
