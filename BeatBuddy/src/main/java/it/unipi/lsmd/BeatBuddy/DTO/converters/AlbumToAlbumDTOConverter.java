package it.unipi.lsmd.BeatBuddy.DTO.converters;

import it.unipi.lsmd.BeatBuddy.DTO.AlbumDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AlbumToAlbumDTOConverter implements Converter<Album, AlbumDTO> {

    @Override
    public AlbumDTO convert(Album album) {
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setId(album.getId());
        albumDTO.setArtists(album.getArtists());
        albumDTO.setCoverURL(album.getCoverURL());
        albumDTO.setTitle(album.getTitle());

        return albumDTO;
    }
}