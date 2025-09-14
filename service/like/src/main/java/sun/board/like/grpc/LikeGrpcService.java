package sun.board.like.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import sun.board.like.entity.LikeCounts;
import sun.board.like.entity.TargetType;
import sun.board.like.repository.ArticleLikeCountRepository;
import sun.board.like.repository.ArticleLikeRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class LikeGrpcService extends LikeQueryServiceGrpc.LikeQueryServiceImplBase {

    private final ArticleLikeCountRepository likeCountsRepo;
    private final ArticleLikeRepository likesRepo;

    @Override
    public void getLikeCounts(LikeCountsRequest request, StreamObserver<LikeCountsResponse> responseObserver) {
        var ids = request.getTargetIdsList();
        var rows = ids.isEmpty() ? List.<LikeCounts>of() : likeCountsRepo.findByTargetIdIn(ids);

        Map<Long, Long> map = rows.stream().collect(Collectors.toMap(
                LikeCounts::getTargetId, LikeCounts::getLikeCount));

        LikeCountsResponse.Builder out = LikeCountsResponse.newBuilder();
        for (long id : ids) {
            out.putCounts(id, map.getOrDefault(id, 0L));
        }

        responseObserver.onNext(out.build());
        responseObserver.onCompleted();

    }

    @Override
    public void getMyLikes(MyLikesRequest req, StreamObserver<MyLikesResponse> respObs) {
        long userId = req.getUserId();
        TargetType type = TargetType.valueOf(req.getTargetType().name());
        var ids = req.getTargetIdsList();

        List<Long> liked = ids.isEmpty()
                ? List.of()
                : likesRepo.findMyLikedTargetIds(userId, type, ids);
        Set<Long> likedSet = new HashSet<>(liked);

        MyLikesResponse.Builder out = MyLikesResponse.newBuilder();
        for (long id : ids) {
            out.addResults(
                    MyLikesResponse.Item.newBuilder()
                            .setTargetId(id)
                            .setLiked(likedSet.contains(id))
                            .build()
            );
        }
        respObs.onNext(out.build());
        respObs.onCompleted();
    }
}
