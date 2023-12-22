import { OwnerProfileDTO } from "owner-api";
import APIFactory from "../api/APIFactory";

type Profile = OwnerProfileDTO;

class ProfileCacheService {
    private static instance: ProfileCacheService;
    private _profile?: Profile;

    private constructor() {}

    public static getInstance(): ProfileCacheService {
      if (!ProfileCacheService.instance) {
        ProfileCacheService.instance = new ProfileCacheService();
      }
      return ProfileCacheService.instance;
    }
  
    private async fetchProfile(request: Request): Promise<Profile> {
        const ownerApi = await APIFactory.createOwnerApi();
        const profile = await ownerApi.getMyProfile({ signal: request.signal });
        return profile;
    }

    public async getProfile(request: Request): Promise<Profile> {
        if (!this._profile) {
            const profile = await this.fetchProfile(request);
            this._profile = profile;
        }
        return this._profile;
    }

    public invalidate() {
        this._profile = undefined;
    }
}

export default ProfileCacheService;
